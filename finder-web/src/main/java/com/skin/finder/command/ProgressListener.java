/*
 * $RCSfile: ProgressListener.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.command;

import java.util.UUID;

/**
 * <p>Title: ProgressListener</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ProgressListener {
    private Progress progress;

    /**
     * 
     */
    public ProgressListener() {
        this.progress = new Progress();
        this.progress.setId(UUID.randomUUID().toString());
    }

    /**
     * @param current
     * @param loaded
     * @param total
     */
    public void update(String current, long loaded, long total) {
        this.progress.update(current, loaded, total);
    }

    /**
     * @param e
     */
    public void error(Exception e) {
        this.progress.error(e);
    }
}
