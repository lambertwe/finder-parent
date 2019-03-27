/*
 * $RCSfile: ZipUtil.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;
import java.util.jar.JarEntry;
import java.util.jar.JarException;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: ZipUtil</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class JarUtil {
    private static final Logger logger = LoggerFactory.getLogger(JarUtil.class);
    private static final int JDK = getJDKVersion();

    /**
     * @param source
     * @param target
     * @param charset
     */
    public static void compress(File source, File target, Charset charset) {
        OutputStream outputStream = null;
        JarOutputStream jarOutputStream = null;

        try {
            outputStream = new FileOutputStream(target);
            jarOutputStream = getJarOutputStream(outputStream, charset);
            compress(source, jarOutputStream, "");

            outputStream.flush();
            jarOutputStream.finish();
            jarOutputStream.flush();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            if(jarOutputStream != null) {
                try {
                    jarOutputStream.close();
                }
                catch(IOException e) {
                }
            }
            if(outputStream != null) {
                try {
                    outputStream.close();
                }
                catch(IOException e) {
                }
            }
        }
    }

    /**
     * @param file
     * @param jarOutputStream
     * @param basedir
     * @throws IOException
     */
    public static void compress(File file, JarOutputStream jarOutputStream, String basedir) throws IOException {
        if(file.isDirectory()) {
            compressDirectory(file, jarOutputStream, basedir);
        }
        else {
            compressFile(file, jarOutputStream, basedir);
        }
    }

    /**
     * @param dir
     * @param jarOutputStream
     * @param basedir
     * @throws IOException
     */
    public static void compressDirectory(File dir, JarOutputStream jarOutputStream, String basedir) throws IOException {
        File[] files = dir.listFiles();

        if(files == null || files.length < 1) {
            logger.debug("{}{}/", basedir, dir.getName(), "/");
            JarEntry entry = new JarEntry(basedir + dir.getName() + "/");
            jarOutputStream.putNextEntry(entry);
            jarOutputStream.closeEntry();
        }
        else {
            for(int i = 0; i < files.length; i++) {
                compress(files[i], jarOutputStream, basedir + dir.getName() + "/");
            }
        }
    }

    /**
     * @param file
     * @param jarOutputStream
     * @param basedir
     * @throws IOException
     */
    public static void compressFile(File file, JarOutputStream jarOutputStream, String basedir) throws IOException {
        InputStream inputStream = null;
        logger.debug("{}{}", basedir, file.getName());

        try {
            inputStream = new FileInputStream(file);
            JarEntry entry = new JarEntry(basedir + file.getName());
            jarOutputStream.putNextEntry(entry);
            copy(inputStream, jarOutputStream, 4096);
        }
        finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                }
                catch(IOException e) {
                }
            }
        }
    }

    /**
     * @param file
     * @param target
     * @param charset
     * @throws IOException
     */
    public static void unzip(File file, File target, Charset charset) throws IOException {
        JarFile zipFile = null;

        try {
            zipFile = getJarFile(file, charset);
            java.util.Enumeration<?> enums = zipFile.entries();
            JarEntry jarEntry = null;

            while(enums.hasMoreElements()) {
                jarEntry = (JarEntry)(enums.nextElement());
                String fileName = jarEntry.getName();
                File temp = new File(target, fileName);

                if(jarEntry.isDirectory()) {
                    temp.mkdirs();
                    continue;
                }

                if(!temp.getParentFile().exists()) {
                    temp.getParentFile().mkdirs();
                }

                InputStream inputStream = null;

                try {
                    inputStream = zipFile.getInputStream(jarEntry);
                    copy(inputStream, temp);

                    /**
                     * setLastModified
                     */
                    long time = jarEntry.getTime();

                    if(time > -1) {
                        temp.setLastModified(time);
                    }
                }
                finally {
                    if(inputStream != null) {
                        inputStream.close();
                    }
                }
            }
        }
        finally {
            if(zipFile != null) {
                try {
                    zipFile.close();
                }
                catch(IOException e) {
                }
            }
        }
    }

    /**
     * @param inputStream
     * @param file
     * @throws IOException
     */
    public static void copy(InputStream inputStream, File file) throws IOException {
        OutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            copy(inputStream, outputStream, 4096);
        }
        finally {
            if(outputStream != null) {
                outputStream.close();
            }
        }
    }

    /**
     * @param inputStream
     * @param outputStream
     * @param bufferSize
     * @throws IOException
     */
    private static void copy(InputStream inputStream, OutputStream outputStream, int bufferSize) throws IOException {
        int length = 0;
        byte[] bytes = new byte[Math.max(bufferSize, 4096)];

        while((length = inputStream.read(bytes)) > -1) {
            outputStream.write(bytes, 0, length);
        }
        outputStream.flush();
    }

    /**
     * @param outputStream
     * @param charset
     * @return JarOutputStream
     * @throws IOException
     */
    public static JarOutputStream getJarOutputStream(OutputStream outputStream, Charset charset) throws IOException {
        if(JDK <= 16 || charset == null) {
            return new JarOutputStream(outputStream);
        }
        else {
            try {
                Constructor<JarOutputStream> c = JarOutputStream.class.getConstructor(OutputStream.class, Charset.class);
                return c.newInstance(outputStream, charset);
            }
            catch(Exception e) {
                logger.error(e.getMessage(), e);
            }
            return new JarOutputStream(outputStream);
        }
    }

    /**
     * @param file
     * @param charset
     * @return JarFile
     * @throws IOException
     * @throws JarException
     */
    public static JarFile getJarFile(File file, Charset charset) throws IOException, JarException {
        if(JDK <= 16 || charset == null) {
            return new JarFile(file);
        }
        else {
            try {
                Constructor<JarFile> c = JarFile.class.getConstructor(File.class, Charset.class);
                return c.newInstance(file, charset);
            }
            catch(Exception e) {
                logger.warn(e.getMessage());
                logger.error(e.getMessage(), e);
            }
            return new JarFile(file);
        }
    }

    /**
     * @return int
     */
    private static int getJDKVersion() {
        int count = 0;
        String version = System.getProperty("java.version");
        logger.info("java.version: {}", version);

        for(int i = 0; i < version.length(); i++) {
            char c = version.charAt(i);

            if(c == '.') {
                count++;

                if(count >= 2) {
                    version = version.substring(0, i);
                    break;
                }
            }
        }

        try {
            return (int)(Float.parseFloat(version) * 10);
        }
        catch(Exception e) {
        }
        return 16;
    }
}
