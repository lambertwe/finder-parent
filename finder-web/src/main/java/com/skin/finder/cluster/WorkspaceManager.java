/*
 * $RCSfile: WorkspaceManager.java,v $
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.cluster.ClusterManager;
import com.skin.finder.cluster.Host;
import com.skin.finder.cluster.Workspace;
import com.skin.finder.config.ConfigFactory;

/**
 * <p>Title: WorkspaceManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class WorkspaceManager {
    private static final Logger logger = LoggerFactory.getLogger(WorkspaceManager.class);

    /**
     * default
     */
    private WorkspaceManager() {
    }

    /**
     * @param name
     * @return String
     */
    public static String getWork(String name) {
        Workspace workspace = getWorkspace(name);
        return (workspace != null ? workspace.getWork() : null);
    }

    /**
     * @param name
     * @return boolean
     */
    public static boolean getReadonly(String name) {
        Workspace workspace = getWorkspace(name);

        if(workspace == null) {
            return true;
        }
        return workspace.getReadonly();
    }

    /**
     * @return List<String>
     */
    public static List<String> getWorkspaces() {
        String hostName = ConfigFactory.getHostName();
        Host self = ClusterManager.getHost(hostName);

        if(self == null) {
            logger.warn("Can't init workspace: {}", hostName);
            return null;
        }

        List<Workspace> workspaces = self.getWorkspaces();
        List<String> names = new ArrayList<String>();

        if(workspaces != null) {
            for(Workspace workspace : workspaces) {
                names.add(workspace.getName());
            }
        }
        return names;
    }

    /**
     * @return Workspace
     */
    private static Workspace getWorkspace(String name) {
        String hostName = ConfigFactory.getHostName();
        Host self = ClusterManager.getHost(hostName);

        if(self == null) {
            logger.warn("{}@{} workspace not exists!", hostName, name);
            return null;
        }
        return self.getWorkspace(name);
    }
}
