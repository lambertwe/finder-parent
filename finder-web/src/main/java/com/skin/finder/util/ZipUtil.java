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
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: ZipUtil</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ZipUtil {
    private static final Logger logger = LoggerFactory.getLogger(ZipUtil.class);
    private static final int JDK = getJDKVersion();

    /**
     * @param source
     * @param target
     * @param charset
     */
    public static void compress(File source, File target, Charset charset) {
        OutputStream outputStream = null;
        ZipOutputStream zipOutputStream = null;

        try {
            outputStream = new FileOutputStream(target);
            zipOutputStream = getZipOutputStream(outputStream, charset);
            compress(source, zipOutputStream, "");

            outputStream.flush();
            zipOutputStream.finish();
            zipOutputStream.flush();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            if(zipOutputStream != null) {
                try {
                    zipOutputStream.close();
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
     * @param zipOutputStream
     * @param basedir
     * @throws IOException
     */
    public static void compress(File file, ZipOutputStream zipOutputStream, String basedir) throws IOException {
        if(file.isDirectory()) {
            compressDirectory(file, zipOutputStream, basedir);
        }
        else {
            compressFile(file, zipOutputStream, basedir);
        }
    }

    /**
     * @param dir
     * @param zipOutputStream
     * @param basedir
     * @throws IOException
     */
    public static void compressDirectory(File dir, ZipOutputStream zipOutputStream, String basedir) throws IOException {
        File[] files = dir.listFiles();

        logger.debug("{}{}/", basedir, dir.getName(), "/");
        ZipEntry entry = new ZipEntry(basedir + dir.getName() + "/");
        zipOutputStream.putNextEntry(entry);
        zipOutputStream.closeEntry();

        if(files != null && files.length > 0) {
            for(int i = 0; i < files.length; i++) {
                compress(files[i], zipOutputStream, basedir + dir.getName() + "/");
            }
        }
    }

    /**
     * @param file
     * @param zipOutputStream
     * @param basedir
     * @throws IOException
     */
    public static void compressFile(File file, ZipOutputStream zipOutputStream, String basedir) throws IOException {
        InputStream inputStream = null;
        logger.debug("{}{}", basedir, file.getName());

        try {
            inputStream = new FileInputStream(file);
            ZipEntry entry = new ZipEntry(basedir + file.getName());
            zipOutputStream.putNextEntry(entry);
            copy(inputStream, zipOutputStream, 4096);
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
        ZipFile zipFile = null;

        try {
            zipFile = getZipFile(file, charset);
            java.util.Enumeration<?> enums = zipFile.entries();
            ZipEntry zipEntry = null;

            while(enums.hasMoreElements()) {
                zipEntry = (ZipEntry)(enums.nextElement());
                String fileName = zipEntry.getName();
                File temp = new File(target, fileName);

                if(zipEntry.isDirectory()) {
                    temp.mkdirs();
                    continue;
                }

                if(!temp.getParentFile().exists()) {
                    temp.getParentFile().mkdirs();
                }

                InputStream inputStream = null;

                try {
                    inputStream = zipFile.getInputStream(zipEntry);
                    copy(inputStream, temp);

                    /**
                     * setLastModified
                     */
                    long time = zipEntry.getTime();

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
     * @return ZipOutputStream
     */
    public static ZipOutputStream getZipOutputStream(OutputStream outputStream, Charset charset) {
        if(JDK <= 16 || charset == null) {
            return new ZipOutputStream(outputStream);
        }
        else {
            try {
                Constructor<ZipOutputStream> c = ZipOutputStream.class.getConstructor(OutputStream.class, Charset.class);
                return c.newInstance(outputStream, charset);
            }
            catch(Exception e) {
                logger.error(e.getMessage(), e);
            }
            return new ZipOutputStream(outputStream);
        }
    }

    /**
     * @param file
     * @param charset
     * @return ZipFile
     * @throws IOException
     * @throws ZipException
     */
    public static ZipFile getZipFile(File file, Charset charset) throws ZipException, IOException {
        if(JDK <= 16 || charset == null) {
            return new ZipFile(file);
        }
        else {
            try {
                Constructor<ZipFile> c = ZipFile.class.getConstructor(File.class, Charset.class);
                return c.newInstance(file, charset);
            }
            catch(Exception e) {
                logger.warn(e.getMessage());
                logger.error(e.getMessage(), e);
            }
            return new ZipFile(file);
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
