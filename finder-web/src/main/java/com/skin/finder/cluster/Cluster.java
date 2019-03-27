/*
 * $RCSfile: Cluster.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.cluster;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Title: Cluster</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Cluster {
    private long version = 1L;
    private Map<String, Host> lookup;

    /**
     * default
     */
    public Cluster() {
        this.lookup = new ConcurrentHashMap<String, Host>();
    }

    /**
     * @return the version
     */
    public long getVersion() {
        return this.version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(long version) {
        this.version = version;
    }

    /**
     * @param host
     */
    public void add(Host host) {
        String name = host.getName();

        if(name == null || (name = name.trim()).length() < 1) {
            throw new NullPointerException("name must be not null");
        }
        this.lookup.put(name, host);
    }

    /**
     * @param name
     * @return Host
     */
    public Host remove(String name) {
        if(name == null) {
            return null;
        }
        return this.lookup.remove(name);
    }

    /**
     * @param name
     * @return Host
     */
    public Host getHost(String name) {
        if(name == null) {
            return null;
        }
        return this.lookup.get(name);
    }

    /**
     * @param name
     * @param workspace
     * @return Workspace
     */
    public Workspace getWorkspace(String name, String workspace) {
        Host host = this.getHost(name);
        return (host != null ? host.getWorkspace(workspace) : null);
    }

    /**
     * @return List<Host>
     */
    public List<Host> getHosts() {
        List<Host> hosts = new ArrayList<Host>();
        hosts.addAll(this.lookup.values());
        java.util.Collections.sort(hosts);
        return hosts;
    }

    /**
     * @return int
     */
    public int size() {
        return this.lookup.size();
    }

    /**
     * @return long
     */
    public long modified() {
        return (this.version = this.version + 1);
    }

    /**
     * destroy
     */
    public void destroy() {
        if(this.lookup != null) {
            Map<String, Host> map = this.lookup;

            for(Map.Entry<String, Host> entry : map.entrySet()) {
                Host host = entry.getValue();
                host.destroy();
            }
            this.lookup.clear();
            this.lookup = null;
        }
    }
}
