/*
 * $RCSfile: OS.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web.command;

/**
 * <p>Title: OS</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class OS {
    /**
     * 
     */
    public static final String NAME = System.getProperty("os.name");

    /**
     * 
     */
    public static final boolean LINUX = (NAME.indexOf("Linux") > -1);

    /**
     * 
     */
    public static final boolean WINDOWS = (NAME.indexOf("Windows") > -1);

    /**
     * @param cmd
     * @return String
     */
    public static String getCMD(String cmd) {
        if(WINDOWS) {
            return "\"" + cmd + "\"";
        }
        return cmd;
    }
}

