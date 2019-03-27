/*
 * $RCSfile: ClusterRestart.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.cluster;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.config.App;
import com.skin.finder.web.command.Restart;

/**
 * <p>Title: ClusterRestart</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ClusterRestart extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(ClusterRestart.class);

    /**
     * 
     */
    public static void execute() {
        new ClusterRestart().start();
    }

    @Override
    public void run() {
        String token = App.getToken();
        Cluster cluster = ClusterManager.getInstance();
        List<Host> hosts = cluster.getHosts();

        if(hosts != null) {
            for(Host host : hosts) {
                String url = host.getUrl() + "?action=agent.node.restart&token=" + token;

                try {
                    API.invoke(url, (String)null);
                }
                catch(IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        Restart.execute(1000L);
    }
}
