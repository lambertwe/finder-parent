/*
 * $RCSfile: Copy.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.command;

import java.io.File;
import java.util.List;

import com.skin.finder.util.IO;

/**
 * copy & move
 * <p>Title: Copy</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Copy {
    private List<File> files;
    private File directory;
    private boolean delete;
    private ProgressListener progressListener;

    /**
     * @throws Exception
     */
    public void execute() throws Exception {
        /**
         * TODO: update progress
         */
        if(this.files == null || this.files.isEmpty()) {
            return;
        }

        if(this.directory == null) {
            return;
        }

        /**
         * TODO: update progress
         */
        for(File file : this.files) {
            File target = new File(this.directory, file.getName());
            IO.copy(file, target, true);

            if(this.delete) {
                IO.delete(file);
            }
        }
    }

    /**
     * @return the files
     */
    public List<File> getFiles() {
        return this.files;
    }

    /**
     * @param files the files to set
     */
    public void setFiles(List<File> files) {
        this.files = files;
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
     * @return the delete
     */
    public boolean isDelete() {
        return this.delete;
    }

    /**
     * @param delete the delete to set
     */
    public void setDelete(boolean delete) {
        this.delete = delete;
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
