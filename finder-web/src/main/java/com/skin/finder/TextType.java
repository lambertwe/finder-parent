/*
 * $RCSfile: TextType.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 * http://www.finderweb.net
 */
package com.skin.finder;

import java.io.IOException;

import com.skin.finder.config.ConfigFactory;
import com.skin.finder.util.StringUtil;

/**
 * <p>Title: TextType</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class TextType {
    /**
     * default
     */
    private TextType() {
    }

    /**
     * @param path
     * @return boolean
     * @throws IOException
     */
    public static boolean allow(String path) throws IOException {
        String allows = ConfigFactory.getTextType();
        String extension = FileType.getExtension(path).toLowerCase();
        return StringUtil.contains(allows, extension);
    }
}
