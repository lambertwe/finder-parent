/*
 * $RCSfile: StringUtil.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: StringUtil</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class StringUtil {
    /**
     * empty string
     */
    public static final String EMPTY = "";

    /**
     * default
     */
    private StringUtil() {
    }

    /**
     * @param text
     * @return boolean
     */
    public static boolean isBlank(String text) {
        if(text == null) {
            return true;
        }

        int length = text.length();

        if(length == 0) {
            return true;
        }

        for(int i = 0; i < length; i++) {
            if(Character.isWhitespace(text.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param text
     * @return boolean
     */
    public static boolean notBlank(String text) {
        return !isBlank(text);
    }

    /**
     * @param s1
     * @param s2
     * @return boolean
     */
    public static boolean equals(String s1, String s2) {
        if(s1 != null) {
            return s1.equals(s2);
        }

        if(s2 != null) {
            return s2.equals(s1);
        }
        return true;
    }

    /**
     * @param content
     * @param value
     * @return boolean
     */
    public static boolean contains(String content, String value) {
        if(content == null) {
            return false;
        }

        if(content.trim().equals("*")) {
            return true;
        }
        return contains(content.split(","), value);
    }

    /**
     * @param array
     * @param value
     * @return boolean
     */
    public static boolean contains(String[] array, String value) {
        if(array == null) {
            return false;
        }

        for(int i = 0; i < array.length; i++) {
            if(array[i] != null) {
                String temp = array[i].trim();

                if(temp.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param source
     * @param limit
     * @param trim
     * @param ignoreWhitespace
     * @return String[]
     */
    public static String[] split(String source, String limit, boolean trim, boolean ignoreWhitespace) {
        int i = 0;
        int j = 0;
        String s = null;
        List<String> list = new ArrayList<String>();

        while((j = source.indexOf(limit, i)) > -1) {
            if(j > i) {
                s = source.substring(i, j);

                if(trim) {
                    s = s.trim();
                }

                if(!ignoreWhitespace || s.length() > 0) {
                    list.add(s);
                }
            }
            i = j + limit.length();
        }

        if(i < source.length()) {
            s = source.substring(i);

            if(trim) {
                s = s.trim();
            }

            if(!ignoreWhitespace || s.length() > 0) {
                list.add(s);
            }
        }
        String[] result = new String[list.size()];
        return list.toArray(result);
    }

    /**
     * @param source
     * @return String
     */
    public static String ltrim(String source) {
        if(source == null) {
            return EMPTY;
        }

        for(int i = 0; i < source.length(); i++) {
            if(!Character.isWhitespace(source.charAt(i))) {
                return source.substring(i);
            }
        }
        return source;
    }

    /**
     * @param source
     * @return String
     */
    public static String rtrim(String source) {
        if(source == null) {
            return EMPTY;
        }

        for(int i = source.length() - 1; i > -1; i--) {
            if(!Character.isWhitespace(source.charAt(i))) {
                return source.substring(0, i + 1);
            }
        }
        return source;
    }

    /**
     * @param source
     * @param search
     * @param replacement
     * @return String
     */
    public static String replace(String source, String search, String replacement) {
        if(source == null) {
            return EMPTY;
        }

        if(search == null) {
            return source;
        }

        int s = 0;
        int e = 0;
        int d = search.length();
        StringBuilder buffer = new StringBuilder();

        while(true) {
            e = source.indexOf(search, s);

            if(e == -1) {
                buffer.append(source.substring(s));
                break;
            }
            buffer.append(source.substring(s, e));
            buffer.append(replacement);
            s = e + d;
        }
        return buffer.toString();
    }

    /**
     * @param source
     * @param context
     * @return String
     */
    public static String replace(String source, Map<String, String> context) {
        return replace(source, context, '$');
    }

    /**
     * @param source
     * @param context
     * @param prefix
     * @return String
     */
    public static String replace(String source, Map<String, String> context, char prefix) {
        char c;
        int length = source.length();
        StringBuilder result = new StringBuilder(4096);
        String name = null;
        String value = null;

        for(int i = 0, j = 0; i < length; i++) {
            c = source.charAt(i);

            if(c == prefix && i < length - 1 && source.charAt(i + 1) == '{') {
                i = i + 2;
                j = source.indexOf('}', i);

                if(j > -1) {
                    name = source.substring(i, j).trim();
                    value = context.get(name);

                    if(value != null) {
                        result.append(value);
                    }
                    i = j;
                }
                else {
                    i = length;
                }
            }
            else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * @param source
     * @param c
     * @return String
     */
    public static String remove(String source, char c) {
        char ch;
        int length = source.length();
        StringBuilder buffer = new StringBuilder(length);

        for(int i = 0; i < length; i++) {
            ch = source.charAt(i);

            if(ch != c) {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }

    /**
     * @param text
     * @return String
     */
    public static String hide(String text) {
        if(text == null) {
            return null;
        }
        return hide(text, (text.length() - 4) / 2, 4, '*');
    }

    /**
     * @param text
     * @param offset
     * @param length
     * @param c
     * @return String
     */
    public static String hide(String text, int offset, int length, char c) {
        if(text == null) {
            return null;
        }

        int i = offset;
        int j = offset + length;
        char[] chars = text.toCharArray();

        if(i < 0 || i >= chars.length) {
            i = 0;
        }

        if(j < 0 || j >= chars.length) {
            j = chars.length;
        }

        for(; i < j; i++) {
            chars[i] = c;
        }
        return new String(chars);
    }

    /**
     * @param source
     * @return String
     */
    public static String escape(String source) {
        if(source == null) {
            return "";
        }

        char c;
        StringBuilder buffer = null;

        for(int i = 0, length = source.length(); i < length; i++) {
            c = source.charAt(i);

            switch (c) {
                case '"': {
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("\\\"");
                    break;
                }
                case '\r': {
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("\\r");
                    break;
                }
                case '\n': {
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("\\n");
                    break;
                }
                case '\t': {
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("\\t");
                    break;
                }
                case '\b': {
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("\\b");
                    break;
                }
                case '\f': {
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("\\f");
                    break;
                }
                case '\\': {
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("\\\\");
                    break;
                }
                default : {
                    if(buffer != null) {
                        buffer.append(c);
                    }
                    break;
                }
            }
        }
        return (buffer != null ? buffer.toString() : source);
    }

    /**
     * @param source
     * @param offset
     * @param length
     * @return StringBuilder
     */
    public static StringBuilder getBuffer(String source, int offset, int length) {
        StringBuilder buffer = new StringBuilder();

        if(length > 0) {
            char[] cbuf = new char[length];
            buffer = new StringBuilder((int)(source.length() * 1.2));
            source.getChars(offset, length, cbuf, 0);
            buffer.append(cbuf, 0, length);
        }
        return buffer;
    }
}
