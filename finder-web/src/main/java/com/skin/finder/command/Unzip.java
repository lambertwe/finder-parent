/*
 * $RCSfile: Unzip.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * <p>Title: Unzip</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Unzip {
    private File source;
    private File directory;
    private ProgressListener progressListener;
    private byte[] buffer;

    /**
     * @throws Exception
     */
    public void execute() throws Exception {
        /**
         * TODO: update progress
         */
        if(this.source == null || !this.source.exists()) {
            return;
        }

        if(this.directory == null) {
            return;
        }
    }

    /**
     * @param file
     * @param target
     * @throws IOException
     */
    protected void unzip(File file, File target) throws IOException {
        ZipFile zipFile = null;
        ZipInputStream zipInputStream = null;
        ZipEntry zipEntry = null;

        try {
            zipFile = new ZipFile(file);
            zipInputStream = new ZipInputStream(new FileInputStream(file));

            if(this.buffer == null) {
                this.buffer = new byte[8192];
            }

            while((zipEntry = zipInputStream.getNextEntry()) != null) {
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

            if(zipInputStream != null) {
                try {
                    zipInputStream.close();
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
    public void copy(InputStream inputStream, File file) throws IOException {
        OutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            copy(inputStream, outputStream);
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
    private void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        int length = 0;
        byte[] buffer = this.buffer;

        while((length = inputStream.read(buffer)) > -1) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
    }

    /**
     * @return the source
     */
    public File getSource() {
        return this.source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(File source) {
        this.source = source;
    }

    /**
     * @return the directory
     */
    public File getDirectory() {
        return this.directory;
    }

    /**
     * @param directory the directory to set
     */
    public void setDirectory(File directory) {
        this.directory = directory;
    }

    /**
     * @return the progressListener
     */
    public ProgressListener getProgressListener() {
        return this.progressListener;
    }

    /**
     * @param progressListener the progressListener to set
     */
    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }
}
