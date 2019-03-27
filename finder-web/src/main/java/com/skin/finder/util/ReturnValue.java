/*
 * $RCSfile: RandomUtil.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

/**
 * <p>Title: ReturnValue</p>
 * <p>Description: </p>
 * @author xuesong.net
 * @version 1.0
 * @param <T>
 */
public class ReturnValue<T> implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int status;
    private String message;
    private T value;

    /**
     * success
     */
    public static final int SUCCESS = 200;

    /**
     * error
     */
    public static final int ERROR = 500;

    /**
     * default
     */
    public ReturnValue() {
    }

    /**
     * @param status
     * @param message
     */
    public ReturnValue(int status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * @param status
     * @param message
     * @param value
     */
    public ReturnValue(int status, String message, T value) {
        this.status = status;
        this.message = message;
        this.value = value;
    }

    /**
     * @param value
     * @return ReturnValue
     */
    public static <E> ReturnValue<E> success(E value) {
        return new ReturnValue<E>(SUCCESS, "success", value);
    }

    /**
     * @param message
     * @param value
     * @return ReturnValue
     */
    public static <E> ReturnValue<E> success(String message, E value) {
        return new ReturnValue<E>(SUCCESS, message, value);
    }

    /**
     * @param message
     * @return ReturnValue<E>
     */
    public static <E> ReturnValue<E> error(String message) {
        return new ReturnValue<E>(ERROR, message);
    }

    /**
     * @return boolean
     */
    public boolean success() {
        return (this.status == 200 || this.status == 0);
    }

    /**
     * @return boolean
     */
    public boolean failed() {
        return !this.success();
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
     * @return the value
     */
    public T getValue() {
        return this.value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(T value) {
        this.value = value;
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
