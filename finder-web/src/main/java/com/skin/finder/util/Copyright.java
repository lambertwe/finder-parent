/*
 * $RCSfile: Copyright.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

/**
 * <p>Title: Copyright</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Copyright {
    /**
     * default
     */
    private Copyright() {
    }

    /**
     * @return String
     */
    public static String getCopyright() {
        StringBuilder buffer =  new StringBuilder();
        buffer.append("================================================================================\r\n");
        buffer.append("Welcome to Finder.\r\n");
        buffer.append("Copyright (C) 2008 Skin, Inc. All rights reserved.\r\n");
        buffer.append("This software is the proprietary information of Skin, Inc.\r\n");
        buffer.append("Use is subject to license terms.\r\n");
        buffer.append("http://www.finderweb.net\r\n");
        buffer.append("================================================================================\r\n");
        return buffer.toString();
    }
}

