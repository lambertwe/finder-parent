/*
 * $RCSfile: ID.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>Title: ID</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ID {
    private static final AtomicLong VALUE = new AtomicLong(0);

    /**
     * disabled
     */
    private ID() {
    }

    /**
     * @return long
     */
    public static long get() {
        return VALUE.get();
    }

    /**
     * @return long
     */
    public static long next() {
        return VALUE.incrementAndGet();
    }
}
