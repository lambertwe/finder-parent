/*
 * $RCSfile: FilePolicyManager.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.acl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.config.ConfigFactory;
import com.skin.finder.util.DateUtil;
import com.skin.finder.util.IO;
import com.skin.finder.util.StringUtil;

/**
 * <p>Title: FilePolicyManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class FilePolicyManager {
    private static final String LOCATION = "policy/file/";
    private static final Logger logger = LoggerFactory.getLogger(FilePolicyManager.class);

    /**
     * @param userPermission
     */
    public static void save(UserPermission userPermission) {
        String userName = userPermission.getUserName();
        File file = ConfigFactory.getFile(LOCATION + userName + ".file.policy");
        String content = build(userPermission);
        logger.info("save: {}, {}", userName, file.getAbsolutePath());
        logger.debug("content: {}", content);

        try {
            IO.write(file, content.getBytes("utf-8"));
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * @param userName
     * @param content
     */
    public static void save(String userName, String content) {
        File file = ConfigFactory.getFile(LOCATION + userName + ".file.policy");
        logger.info("save: {}, {}", userName, file.getAbsolutePath());
        logger.debug("content: {}", content);

        try {
            IO.write(file, content.getBytes("utf-8"));
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * @param userName
     * @return UserPermission
     */
    public static UserPermission getByUserName(String userName) {
        InputStream inputStream = ConfigFactory.getInputStream(LOCATION + userName + ".file.policy");

        if(inputStream != null) {
            return parse(userName, inputStream);
        }
        return new UserPermission(userName);
    }

    /**
     * @param userName
     * @param file
     * @return UserPermission
     */
    public static UserPermission parse(String userName, File file) {
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);
            return parse(userName, inputStream);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally {
            IO.close(inputStream);
        }
        return null;
    }

    /**
     * @param userName
     * @param inputStream
     * @return UserPermission
     */
    public static UserPermission parse(String userName, InputStream inputStream) {
        InputStreamReader inputStreamReader = null;

        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            return parse(userName, inputStreamReader);
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            IO.close(inputStreamReader);
        }
        return new UserPermission(userName);
    }

    /**
     * @param userName
     * @param reader
     * @return UserPermission
     */
    public static UserPermission parse(String userName, Reader reader) {
        BufferedReader bufferedReader = null;
        UserPermission userPermission = new UserPermission(userName);

        try {
            String line;
            bufferedReader = new BufferedReader(reader);

            while((line = bufferedReader.readLine()) != null) {
                line = line.trim();

                if(line.length() < 1 || line.startsWith("#")) {
                    continue;
                }

                if(!line.endsWith("{")) {
                    throw new RuntimeException("bad sytax, expect '{'");
                }

                line = line.substring(0, line.length() - 1);

                String[] array = StringUtil.split(line, " ", true, true);
                String grant = array[0];
                String action = array[1];
                String on = array[2];
                String workspace = array[3];

                if(!workspace.startsWith("localhost@")) {
                    continue;
                }

                workspace = workspace.substring(10);
                Permission permission = getFileSet(bufferedReader);
                permission.setUserName(userName);
                permission.setAction(action);
                permission.setWorkspace(workspace);

                if(!grant.equalsIgnoreCase("grant")) {
                    continue;
                }

                if(!on.equalsIgnoreCase("on")) {
                    continue;
                }

                String key = action + "@" + workspace;
                userPermission.add(key, permission);
            }
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            IO.close(bufferedReader);
        }
        return userPermission;
    }

    /**
     * @param bufferedReader
     * @return Permission
     * @throws IOException
     */
    private static Permission getFileSet(BufferedReader bufferedReader) throws IOException {
        String line = null;
        Permission fileSet = new Permission();
        List<String> includes = new ArrayList<String>();
        List<String> excludes = new ArrayList<String>();

        while((line = bufferedReader.readLine()) != null) {
            line = line.trim();

            if(line.length() < 1 || line.startsWith("#")) {
                continue;
            }

            if(line.equals("}") || line.equals("};")) {
                break;
            }

            if(line.startsWith("include")) {
                includes.add(line.substring(7).trim());
            }
            else if(line.startsWith("exclude")) {
                excludes.add(line.substring(7).trim());
            }
            else {
                logger.warn("unkonwn: {}", line);
            }
        }

        fileSet.setIncludes(includes);
        fileSet.setExcludes(excludes);
        return fileSet;
    }

    /**
     * @param userPermission
     * @return String
     */
    public static String build(UserPermission userPermission) {
        String userName = userPermission.getUserName();
        String timestamp = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        StringBuilder buffer = new StringBuilder();
        buffer.append("############################################################\r\n");
        buffer.append("# ").append(userPermission.getUserName()).append(" host\r\n");
        buffer.append("# \r\n");
        buffer.append("# date: ").append(timestamp).append("\r\n");
        buffer.append("############################################################\r\n");
        buffer.append("\r\n");
        buffer.append("# 路径配置遵循ant路径配置规则\r\n");
        buffer.append("# host@workspace: host必须是localhost, 并且只能是localhost\r\n");
        buffer.append("# ==========================================================\r\n");

        List<String> keys = userPermission.getKeys();

        for(String domain : keys) {
            Permission permission = userPermission.getPermission(domain);

            if(permission != null) {
                build(buffer, userName, permission);
            }
        }
        return buffer.toString();
    }

    /**
     * @param buffer
     * @param userName
     * @param permission
     * @return StringBuilder
     */
    public static StringBuilder build(StringBuilder buffer, String userName, Permission permission) {
        String action = permission.getAction();
        String workspace = permission.getWorkspace();
        List<String> includes = permission.getIncludes();
        List<String> excludes = permission.getExcludes();
        buffer.append("# ").append(action).append("\r\n");
        buffer.append("grant ").append(action).append(" on localhost@").append(workspace).append(" {\r\n");

        if(includes != null && includes.size() > 0) {
            for(String pattern : includes) {
                buffer.append("    include ");
                buffer.append(pattern);
                buffer.append("\r\n");
            }
        }

        if(excludes != null && excludes.size() > 0) {
            for(String pattern : excludes) {
                buffer.append("    exclude ");
                buffer.append(pattern);
                buffer.append("\r\n");
            }
        }
        buffer.append("}\r\n\r\n");
        return buffer;
    }
}
