package com.skin.finder.web;

import java.util.HashMap;

/**
 * <p>Title: MemResourceManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class MemResourceManager extends ResourceManager {
    /**
     * @param file
     */
    public MemResourceManager(String file) {
        super(file);
        this.cache = new HashMap<String, ContentEntry>(512);
    }
}

