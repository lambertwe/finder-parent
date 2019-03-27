/*
 * $RCSfile: Self.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web.command;

import java.lang.management.ManagementFactory;

/**
 * <p>Title: Self</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Self {
    /**
     * @param delay
     */
    public static void kill(long delay) {
        String processId = getProcessId();
        new Kill(processId, delay).start();
    }

    /**
     * @return String
     */
    public static String getProcessId() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return name.split("@")[0];
    }
}
