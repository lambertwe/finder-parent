/*
 * $RCSfile: Kill.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: Kill</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Kill extends Thread {
    private String processId;
    private long delay = 0L;
    private static final Logger logger = LoggerFactory.getLogger(Kill.class);

    /**
     * @param processId
     */
    public Kill(String processId) {
        this.processId = processId;
    }

    /**
     * @param processId
     * @param delay
     */
    public Kill(String processId, long delay) {
        this.processId = processId;
        this.delay = delay;
    }

    /**
     * execute
     */
    @Override
    public void run() {
        if(this.delay > 0L) {
            try {
                Thread.sleep(this.delay);
            }
            catch(InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }

        if(this.processId == null || this.processId.length() < 1) {
            return;
        }
        Kill.execute(this.processId);
    }

    /**
     * @param processId
     * @param delay
     */
    public static void kill(String processId, long delay) {
        new Kill(processId, delay).start();
    }

    /**
     * 杀进程
     * @param processId
     */
    public static void execute(String processId) {
        if(OS.WINDOWS) {
            try {
                logger.info("taskkill /F /pid {}", processId);
                ProcessBuilder processBuilder = new ProcessBuilder("taskkill", "/F", "/pid", processId);
                Process process = processBuilder.start();

                new ReadThread("errout", process.getErrorStream()).start();
                new ReadThread("stdout" , process.getInputStream()).start();

                int exitCode = process.waitFor();
                logger.info("exitCode: {}", exitCode);
            }
            catch(Exception e) {
                logger.error(e.getMessage(), e);
            }
            return;
        }
        else {
            try {
                logger.info("kill -9 {}", processId);
                ProcessBuilder processBuilder = new ProcessBuilder("kill", "-9", processId);
                Process process = processBuilder.start();

                new ReadThread("errout", process.getErrorStream()).start();
                new ReadThread("stdout" , process.getInputStream()).start();

                int exitCode = process.waitFor();
                logger.info("exitCode: {}", exitCode);
            }
            catch(Exception e) {
                logger.error(e.getMessage(), e);
            }
            return;
        }
    }
}
