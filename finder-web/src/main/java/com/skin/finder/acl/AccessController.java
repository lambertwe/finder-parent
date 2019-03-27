/*
 * $RCSfile: AccessController.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2005 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.acl;

import com.skin.finder.Operation;
import com.skin.finder.cluster.WorkspaceManager;
import com.skin.finder.config.ConfigFactory;

/**
 * <p>Title: AccessController</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public abstract class AccessController {
    // private static final Logger logger = LoggerFactory.getLogger(AccessController.class);
    private static final String ROOT = ConfigFactory.getAdmin();

    /**
     * @param userName
     * @param workspace
     * @param path
     * @return int
     */
    public static int getMode(String userName, String workspace, String path) {
        if(!getRead(userName, workspace, path)) {
            return Operation.NONE;
        }

        if(WorkspaceManager.getReadonly(workspace)) {
            return Operation.READ;
        }

        int mode = Operation.READ;

        if(getWrite(userName, workspace, path)) {
            mode |= Operation.WRITE;
        }

        if(getDelete(userName, workspace, path)) {
            mode |= Operation.DELETE;
        }

        if(getDownload(userName, workspace, path)) {
            mode |= Operation.DOWNLOAD;
        }
        return mode;
    }

    /**
     * @param userName
     * @param workspace
     * @param path
     * @return boolean
     */
    public static boolean getRead(String userName, String workspace, String path) {
        if(userName.equals(ROOT)) {
            return true;
        }
        return has(userName, workspace, "read", path);
    }

    /**
     * @param userName
     * @param workspace
     * @param path
     * @return boolean
     */
    public static boolean getWrite(String userName, String workspace, String path) {
        if(userName.equals(ROOT)) {
            return true;
        }
        return has(userName, workspace, "write", path);
    }

    /**
     * @param userName
     * @param workspace
     * @param path
     * @return boolean
     */
    public static boolean getDelete(String userName, String workspace, String path) {
        if(userName.equals(ROOT)) {
            return true;
        }
        return has(userName, workspace, "delete", path);
    }

    /**
     * @param userName
     * @param workspace
     * @param path
     * @return boolean
     */
    public static boolean getDownload(String userName, String workspace, String path) {
        if(userName.equals(ROOT)) {
            return true;
        }
        return has(userName, workspace, "download", path);
    }

    /**
     * @param userName
     * @param workspace
     * @param action
     * @param path
     * @return boolean
     */
    public static boolean has(String userName, String workspace, String action, String path) {
        UserPermission userPermission = PermissionManager.getPermission(userName);
        Permission permission = userPermission.getPermission(action + "@" + workspace);

        if(permission == null) {
            return false;
        }

        if(path.length() < 1) {
            return permission.match("/");
        }

        if(path.charAt(0) != '/') {
            return permission.match("/" + path);
        }
        else {
            return permission.match(path);
        }
    }
}
