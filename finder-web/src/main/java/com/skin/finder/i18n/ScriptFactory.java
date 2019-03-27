/*
 * $RCSfile: ScriptFactory.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.i18n;

import java.util.Map;

import com.skin.finder.util.StringUtil;

/**
 * <p>Title: ScriptFactory</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ScriptFactory {
    /**
     * @param properties
     * @return String
     */
    public static String build(Map<String, String> properties) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("(function() {\r\n");
        buffer.append("if(typeof(I18N) == \"undefined\") {\r\n");
        buffer.append("    return;\r\n");
        buffer.append("}\r\n");
        buffer.append("I18N.bundle = {\r\n");

        for(Map.Entry<?, ?> entry : properties.entrySet()) {
            String name = entry.getKey().toString();
            String value = entry.getValue().toString();

            buffer.append("\"");
            buffer.append(name);
            buffer.append("\": \"");
            buffer.append(StringUtil.escape(value));
            buffer.append("\",\r\n");
        }

        buffer.append("\"finder.lang.end\": \"ok\"\r\n};\r\n");
        buffer.append("})();\r\n");
        return buffer.toString();
    }
}
