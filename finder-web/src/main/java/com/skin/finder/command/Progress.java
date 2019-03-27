/*
 * $RCSfile: Progress.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.command;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 操作进度
 * <p>Title: Progress</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Progress {
    private String id;
    private String current;
    private long total;
    private long loaded;
    private int status;
    private String message;
    private AtomicBoolean running;

    private static final int ERROR = 500;
    private static final int COMPLETE = 299;

    /**
     * @param current
     * @param loaded
     * @param total
     */
    public void update(String current, long loaded, long total) {
        this.current = current;
        this.loaded = loaded;
        this.total = total;
        this.status = 200;
        this.message = null;
        this.running = new AtomicBoolean(true);
    }

    /**
     * @param e
     */
    public void error(Exception e) {
        this.status = 500;
        this.message = e.getMessage();
    }

    /**
     * 
     */
    public void complete() {
        this.loaded = this.total;
        this.status = COMPLETE;
        this.message = "complete";
    }

    /**
     * @return boolean
     */
    public boolean getError() {
        return (this.status == ERROR);
    }

    /**
     * @return boolean
     */
    public boolean getComplete() {
        return (this.status == COMPLETE);
    }

    /**
     * 
     */
    public void cancel() {
        this.running.set(false);
    }

    /**
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the current
     */
    public String getCurrent() {
        return this.current;
    }

    /**
     * @param current the current to set
     */
    public void setCurrent(String current) {
        this.current = current;
    }

    /**
     * @return the total
     */
    public long getTotal() {
        return this.total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * @return the loaded
     */
    public long getLoaded() {
        return this.loaded;
    }

    /**
     * @param loaded the loaded to set
     */
    public void setLoaded(long loaded) {
        this.loaded = loaded;
    }

    
    /**
     * @return the status
     */
    public int getStatus() {
        return this.status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
