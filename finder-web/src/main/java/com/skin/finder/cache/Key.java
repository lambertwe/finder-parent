/*
 * $RCSfile: Key.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.cache;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: Key</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Key {
    private String key;
    private long version;

    /**
     * 
     */
    public Key() {
    }

    /**
     * @param key
     * @param version
     */
    public Key(String key, long version) {
        this.key = key;
        this.version = version;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return this.key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the version
     */
    public long getVersion() {
        return this.version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(long version) {
        this.version = version;
    }

    /**
     * @param args
     * @return String
     */
    @SuppressWarnings("unchecked")
    public static String build(Object ... args) {
        StringBuilder buffer = new StringBuilder();

        for(int i = 0, length = args.length - 1; i < length; i++) {
            buffer.append(args[i]);
            buffer.append(":");
        }

        if(args.length > 0) {
            Object temp = args[args.length - 1];

            if(temp instanceof Map<?, ?>) {
                buffer.append(build((Map<String, Object>)(temp)));
            }
            else if(temp instanceof List<?>) {
                buffer.append(build((List<?>)(temp)));
            }
            else {
                buffer.append(toString(temp));
            }
        }
        return buffer.toString();
    }

    /**
     * @param parameters
     * @return String
     */
    public static String build(Map<String, Object> parameters) {
        List<String> keys = new ArrayList<String>();

        for(String key : parameters.keySet()) {
            keys.add(key);
        }

        java.util.Collections.sort(keys);
        StringBuilder buffer = new StringBuilder();

        for(Object key : keys) {
            Object value = parameters.get(key);
            buffer.append(key);
            buffer.append(":");
            buffer.append(toString(value));
            buffer.append(":");
        }

        if(buffer.length() > 0) {
            buffer.deleteCharAt(buffer.length() - 1);
        }
        return buffer.toString();
    }

    /**
     * @param c
     * @return String
     */
    public static String build(List<?> c) {
        StringBuilder buffer = new StringBuilder();

        for(Object key : c) {
            buffer.append(toString(key));
            buffer.append(":");
        }

        if(buffer.length() > 0) {
            buffer.deleteCharAt(buffer.length() - 1);
        }
        return buffer.toString();
    }

    /**
     * @param value
     * @return String
     */
    public static String toString(Object value) {
        if(value == null) {
            return "null";
        }

        if(value instanceof Date) {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            return dateFormat.format((Date)(value));
        }

        char c;
        String key = value.toString();
        StringBuilder buffer = new StringBuilder();

        for(int i = 0, length = key.length(); i < length; i++) {
            c = key.charAt(i);

            switch(c) {
                case '\0': {
                    break;
                }
                case '\r': {
                    break;
                }
                case '\n': {
                    break;
                }
                case ' ': {
                    break;
                }
                default: {
                    buffer.append(c);
                }
            }
        }
        return buffer.toString();
    }
}
