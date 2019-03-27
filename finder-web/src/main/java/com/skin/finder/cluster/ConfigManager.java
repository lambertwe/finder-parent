/*
 * $RCSfile: ConfigManager.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.cluster;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.config.ConfigFactory;
import com.skin.finder.util.ReturnValue;
import com.skin.finder.util.URLParameter;

/**
 * <p>Title: ConfigManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);

    /**
     * @param url
     * @param masterName
     * @param hostName
     * @return boolean
     */
    public static boolean rename(String url, String masterName, String hostName) {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("masterName", masterName);
            params.put("hostName", hostName);

            String data = URLParameter.build(params, "utf-8");
            ReturnValue<?> returnValue = API.invoke(url + "?action=agent.node.rename", data);
            return returnValue.success();
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * @param name
     * @param value
     * @throws IOException
     */
    public static void sync(String name, String value) throws IOException {
        Cluster cluster = ClusterManager.getInstance();
        List<Host> hosts = cluster.getHosts();
        String data = URLParameter.build(name, value, "utf-8");

        for(Host host : hosts) {
            String url = host.getUrl();
            ReturnValue<?> returnValue = API.invoke(url + "?action=agent.setConfig", data);

            if(returnValue.failed()) {
                throw new RuntimeException(host.getName() + " sync failed !");
            }
        }
    }

    /**
     * @param params
     * @throws IOException
     */
    public static void sync(Map<String, ?> params) throws IOException {
        String masterName = ConfigFactory.getMaster();
        Cluster cluster = ClusterManager.getInstance();
        List<Host> hosts = cluster.getHosts();
        String data = URLParameter.build(params, "utf-8");
        Set<String> hashSet = new HashSet<String>();

        for(Host host : hosts) {
            String name = host.getName();
            String url = host.getUrl();

            if(masterName.equals(name) || hashSet.contains(url)) {
                continue;
            }

            logger.info("sync - host: {}, url: {}", name, url);
            hashSet.add(url);
            ReturnValue<?> returnValue = API.invoke(url + "?action=agent.conf.sync", data);

            if(returnValue.failed()) {
                throw new RuntimeException(host.getName() + " sync failed !");
            }
        }
    }
}
