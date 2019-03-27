/*
 * $RCSfile: FileItem.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder;

/**
 * <p>Title: FileItem</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ZipItem {
    private String fileName;
    private long fileSize;
    private long compressedSize;
    private long lastModified;
    private String crc;
    private boolean isFile;

    /**
     * @return the fileName
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the fileSize
     */
    public long getFileSize() {
        return this.fileSize;
    }

    /**
     * @param fileSize the fileSize to set
     */
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @return the compressedSize
     */
    public long getCompressedSize() {
        return this.compressedSize;
    }

    /**
     * @param compressedSize the compressedSize to set
     */
    public void setCompressedSize(long compressedSize) {
        this.compressedSize = compressedSize;
    }

    /**
     * @return the lastModified
     */
    public long getLastModified() {
        return this.lastModified;
    }

    /**
     * @param lastModified the lastModified to set
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * @return the crc
     */
    public String getCrc() {
        return this.crc;
    }

    /**
     * @param crc the crc to set
     */
    public void setCrc(String crc) {
        this.crc = crc;
    }

    /**
     * @return the isFile
     */
    public boolean getIsFile() {
        return this.isFile;
    }

    /**
     * @param isFile the isFile to set
     */
    public void setIsFile(boolean isFile) {
        this.isFile = isFile;
    }
}
