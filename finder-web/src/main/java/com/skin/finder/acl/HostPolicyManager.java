/*
 * $RCSfile: HostPolicyManager.java,v $
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
 * <p>Title: HostPolicyManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class HostPolicyManager {
    private static final String LOCATION = "policy/host/";
    private static final Logger logger = LoggerFactory.getLogger(HostPolicyManager.class);

    /**
     * 从本地磁盘加载
     * @param userName
     * @return UserPermission
     */
    public static List<String> load(String userName) {
        InputStream inputStream = ConfigFactory.getInputStream(LOCATION + userName + ".host.policy");

        if(inputStream == null) {
            return null;
        }

        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        List<String> hosts = new ArrayList<String>();

        try {
            String line;
            String keyword;
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            bufferedReader = new BufferedReader(inputStreamReader);

            while((line = bufferedReader.readLine()) != null) {
                line = line.trim();

                if(line.length() < 1 || line.startsWith("#")) {
                    continue;
                }

                String[] array = StringUtil.split(line, " ", true, true);
                keyword = array[0];

                if(!keyword.equalsIgnoreCase("grant")) {
                    throw new RuntimeException("bad command, expect 'grant'.");
                }

                keyword = array[1];

                if(!keyword.equalsIgnoreCase("host")) {
                    throw new RuntimeException("bad command, expect 'grant'.");
                }

                String host = array[2];

                if(host.endsWith(";")) {
                    host = host.substring(0, host.length() - 1).trim();
                }
                logger.debug("grant host on {} to {};", host, userName);
                hosts.add(host);
            }
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            IO.close(bufferedReader);
            IO.close(inputStreamReader);
        }
        return hosts;
    }

    /**
     * @param userName
     * @param hosts
     */
    public static void save(String userName, List<String> hosts) {
        String content = build(userName, hosts);
        File file = ConfigFactory.getFile(LOCATION + userName + ".host.policy");
        logger.info("save: {}, {}", userName, file.getAbsolutePath());
        logger.debug("content: {}", content);

        try {
            IO.write(file, content.getBytes("utf-8"));
        }
        catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * @param userName
     * @param hosts
     * @return String
     */
    public static String build(String userName, List<String> hosts) {
        String timestamp = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        StringBuilder buffer = new StringBuilder();
        buffer.append("############################################################\r\n");
        buffer.append("# ").append(userName).append("host\r\n");
        buffer.append("# \r\n");
        buffer.append("# date: ").append(timestamp).append("\r\n");
        buffer.append("############################################################\r\n");
        buffer.append("\r\n");
        buffer.append("# 用户允许访问的host, host的name必须存在于host.xml\r\n");
        buffer.append("# ==========================================================\r\n");

        if(hosts != null && hosts.size() > 0) {
            for(String host : hosts) {
                buffer.append("grant host ");
                buffer.append(host);
                buffer.append(" to ");
                buffer.append(userName);
                buffer.append(";\r\n");
            }
        }
        buffer.append("\r\n");
        return buffer.toString();
    }
}
