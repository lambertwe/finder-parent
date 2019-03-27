/*
 * $RCSfile: Config.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Title: Config</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Config {
    private Map<String, String> parameters = new ConcurrentHashMap<String, String>();

    /**
     * new instance
     */
    public Config() {
    }

    /**
     * @param map
     */
    public Config(Map<String, String> map) {
        this.parameters.putAll(map);
    }

    /**
     * @param properties
     */
    public Config(Properties properties) {
        Set<Map.Entry<Object, Object>> set = properties.entrySet();

        for(Map.Entry<Object, Object> entry : set) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            if(key != null && value != null) {
                this.parameters.put(key.toString(), value.toString());
            }
        }
    }

    /**
     * @param name
     * @param value
     */
    public void setValue(String name, String value) {
        if(name == null) {
            return;
        }

        if(value != null) {
            this.parameters.put(name, value);
        }
        else {
            this.parameters.put(name, "");
        }
    }

    /**
     * @param name
     * @return String
     */
    public String getValue(String name) {
        return this.parameters.get(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public String getValue(String name, String defaultValue) {
        String value = this.parameters.get(name);

        if(value != null) {
            value = value.trim();
        }

        if(value != null && value.length() > 0) {
            return value;
        }
        return defaultValue;
    }

    /**
     * @param name
     * @return String
     */
    public String getString(String name) {
        return this.getValue(name, null);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public String getString(String name, String defaultValue) {
        return this.getValue(name, defaultValue);
    }

    /**
     * @param name
     * @return Character
     */
    public Character getCharacter(String name) {
        return getCharacter(name, null);
    }

    /**
     * @param name
     * @param defaultValue
     * @return Character
     */
    public Character getCharacter(String name, Character defaultValue) {
        String value = this.getValue(name);

        if(value == null) {
            return defaultValue;
        }
        return parseCharacter(value, defaultValue);
    }

    /**
     * @param name
     * @return Boolean
     */
    public Boolean getBoolean(String name) {
        return this.getBoolean(name, Boolean.FALSE);
    }

    /**
     * @param name
     * @param defaultValue
     * @return Boolean
     */
    public Boolean getBoolean(String name, Boolean defaultValue) {
        String value = this.getValue(name);

        if(value == null) {
            return defaultValue;
        }
        return parseBoolean(value, defaultValue);
    }

    /**
     * @param name
     * @return Byte
     */
    public Byte getByte(String name) {
        return getByte(name, null);
    }

    /**
     * @param name
     * @param defaultValue
     * @return Byte
     */
    public Byte getByte(String name, Byte defaultValue) {
        String value = this.getValue(name);

        if(value == null) {
            return defaultValue;
        }
        return parseByte(value, defaultValue);
    }

    /**
     * @param name
     * @return Short
     */
    public Short getShort(String name) {
        return getShort(name, null);
    }

    /**
     * @param name
     * @param defaultValue
     * @return Short
     */
    public Short getShort(String name, Short defaultValue) {
        String value = this.getValue(name);

        if(value == null) {
            return defaultValue;
        }
        return parseShort(value, defaultValue);
    }

    /**
     * @param name
     * @return Integer
     */
    public Integer getInteger(String name) {
        return getInteger(name, null);
    }

    /**
     * @param name
     * @param defaultValue
     * @return Integer
     */
    public Integer getInteger(String name, Integer defaultValue) {
        String value = this.getValue(name);

        if(value == null) {
            return defaultValue;
        }
        return parseInt(value, defaultValue);
    }

    /**
     * @param name
     * @return Float
     */
    public Float getFloat(String name) {
        return getFloat(name, null);
    }

    /**
     * @param name
     * @param defaultValue
     * @return Float
     */
    public Float getFloat(String name, Float defaultValue) {
        String value = this.getValue(name);

        if(value == null) {
            return defaultValue;
        }
        return parseFloat(value, defaultValue);
    }

    /**
     * @param name
     * @return Double
     */
    public Double getDouble(String name) {
        return getDouble(name, null);
    }

    /**
     * @param name
     * @param defaultValue
     * @return Double
     */
    public Double getDouble(String name, Double defaultValue) {
        String value = this.getValue(name);

        if(value == null) {
            return defaultValue;
        }
        return parseDouble(value, defaultValue);
    }

    /**
     * @param name
     * @return Long
     */
    public Long getLong(String name) {
        return getLong(name, null);
    }

    /**
     * @param name
     * @param defaultValue
     * @return Long
     */
    public Long getLong(String name, Long defaultValue) {
        String value = this.getValue(name);

        if(value == null) {
            return defaultValue;
        }
        return parseLong(value, defaultValue);
    }

    /**
     * @param name
     * @param pattern
     * @return java.util.Date
     */
    public java.util.Date getDate(String name, String pattern) {
        String value = this.getValue(name);

        if(value == null) {
            return null;
        }
        return parseDate(value, pattern);
    }

    /**
     * @param value
     * @return Character
     */
    public Character parseCharacter(String value) {
        return parseCharacter(value, null);
    }

    /**
     * @param value
     * @return Boolean
     */
    public Boolean parseBoolean(String value) {
        return parseBoolean(value, null);
    }

    /**
     * @param value
     * @return Byte
     */
    public Byte parseByte(String value) {
        return parseByte(value, null);
    }

    /**
     * @param value
     * @return Short
     */
    public Short parseShort(String value) {
        return parseShort(value, null);
    }

    /**
     * @param value
     * @return Integer
     */
    public Integer parseInt(String value) {
        return parseInt(value, null);
    }

    /**
     * @param value
     * @return Float
     */
    public Float parseFloat(String value) {
        return parseFloat(value, null);
    }

    /**
     * @param value
     * @return Double
     */
    public Double parseDouble(String value) {
        return parseDouble(value, null);
    }

    /**
     * @param value
     * @return Long
     */
    public Long parseLong(String value) {
        return parseLong(value, null);
    }

    /**
     * @param value
     * @param defaultValue
     * @return Character
     */
    public Character parseCharacter(String value, Character defaultValue) {
        Character result = defaultValue;

        if(value != null && value.trim().length() > 0) {
            try {
                char c = Character.valueOf(value.trim().charAt(0));
                result = Character.valueOf(c);
            }
            catch(Exception e) {
            }
        }
        return result;
    }

    /**
     * @param value
     * @param defaultValue
     * @return Boolean
     */
    public Boolean parseBoolean(String value, Boolean defaultValue) {
        if(value != null) {
            return (value.equalsIgnoreCase("true") ? Boolean.TRUE : Boolean.FALSE);
        }
        return defaultValue;
    }

    /**
     * @param value
     * @param defaultValue
     * @return Byte
     */
    public Byte parseByte(String value, Byte defaultValue) {
        Byte result = defaultValue;

        if(value != null) {
            try {
                byte b = Byte.parseByte(value);
                result = Byte.valueOf(b);
            }
            catch(NumberFormatException e) {
            }
        }
        return result;
    }

    /**
     * @param value
     * @param defaultValue
     * @return parseShort
     */
    public Short parseShort(String value, Short defaultValue) {
        Short result = defaultValue;

        if(value != null) {
            try {
                short s = Short.parseShort(value);
                result = Short.valueOf(s);
            }
            catch(NumberFormatException e) {
            }
        }
        return result;
    }

    /**
     * @param value
     * @param defaultValue
     * @return Integer
     */
    public Integer parseInt(String value, Integer defaultValue) {
        Integer result = defaultValue;

        if(value != null) {
            try {
                int i = Integer.parseInt(value);
                result = Integer.valueOf(i);
            }
            catch(NumberFormatException e) {
            }
        }
        return result;
    }

    /**
     * @param value
     * @param defaultValue
     * @return Float
     */
    public Float parseFloat(String value, Float defaultValue) {
        Float result = defaultValue;

        if(value != null) {
            try {
                float f = Float.parseFloat(value);
                result = new Float(f);
            }
            catch(NumberFormatException e) {
            }
        }
        return result;
    }

    /**
     * @param value
     * @param defaultValue
     * @return Double
     */
    public Double parseDouble(String value, Double defaultValue) {
        Double result = defaultValue;

        if(value != null) {
            try {
                double d = Double.parseDouble(value);
                result = new Double(d);
            }
            catch(NumberFormatException e) {
            }
        }
        return result;
    }

    /**
     * @param value
     * @param defaultValue
     * @return Long
     */
    public Long parseLong(String value, Long defaultValue) {
        Long result = defaultValue;

        if(value != null) {
            try {
                long l = Long.parseLong(value);
                result = Long.valueOf(l);
            }
            catch(NumberFormatException e) {
            }
        }
        return result;
    }

    /**
     * @param value
     * @param format
     * @return java.util.Date
     */
    public java.util.Date parseDate(String value, String format) {
        java.util.Date date = null;

        if(value != null) {
            try {
                java.text.DateFormat df = new java.text.SimpleDateFormat(format);
                date = df.parse(value);
            }
            catch(java.text.ParseException e) {
            }
        }
        return date;
    }

    /**
     * @param <T>
     * @param name
     * @param type
     * @return T
     */
    @SuppressWarnings("unchecked")
    public <T> T getObject(String name, Class<T> type) {
        Object value = null;
        String className = type.getName();

        if(className.equals("char") || className.equals("java.lang.Character")) {
            value = getCharacter(name);
        }
        else if(className.equals("boolean") || className.equals("java.lang.Boolean")) {
            value = getBoolean(name);
        }
        else if(className.equals("byte") || className.equals("java.lang.Byte")) {
            value = getByte(name);
        }
        else if(className.equals("short") || className.equals("java.lang.Short")) {
            value = getShort(name);
        }
        else if(className.equals("int") || className.equals("java.lang.Integer")) {
            value = getInteger(name);
        }
        else if(className.equals("float") || className.equals("java.lang.Float")) {
            value = getFloat(name);
        }
        else if(className.equals("double") || className.equals("java.lang.Double")) {
            value = getDouble(name);
        }
        else if(className.equals("long") || className.equals("java.lang.Long")) {
            value = getLong(name);
        }
        else if(className.equals("java.lang.String")) {
            value = getString(name);
        }
        else if(className.equals("java.util.Date")) {
            value = getDate(name, "yyyy-MM-dd hh:mm:ss");
        }
        return (T)value;
    }

    /**
     * @param name
     * @return boolean
     */
    public boolean has(String name) {
        return this.parameters.containsKey(name);
    }

    /**
     * @param name
     * @param value
     * @return boolean
     */
    public boolean contains(String name, String value) {
        String content = this.getString(name);

        if(content != null) {
            if(content.trim().equals("*")) {
                return true;
            }

            String[] array = content.split(",");

            for(int i = 0; i < array.length; i++) {
                array[i] = array[i].trim();

                if(array[i].equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return Set<String>
     */
    public Set<String> getNames() {
        return this.parameters.keySet();
    }

    /**
     * @param map
     */
    public void extend(Map<String, String> map) {
        this.parameters.putAll(map);
    }

    /**
     * @param config
     */
    public void extend(Config config) {
        this.parameters.putAll(config.parameters);
    }

    /**
     * @param config
     */
    public void copy(Config config) {
        this.parameters.putAll(config.parameters);
    }

    /**
     * @return int
     */
    public int size() {
        return this.parameters.size();
    }

    /**
     *
     */
    public void clear() {
        this.parameters.clear();
    }

    /**
     * @return Map<String, String>
     */
    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.putAll(this.parameters);
        return map;
    }
}
