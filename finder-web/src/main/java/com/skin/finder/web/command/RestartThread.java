/*
 * $RCSfile: Restart.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web.command;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.util.ClassUtil;

/**
 * <p>Title: RestartThread</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class RestartThread extends Thread {
    private long delay = 0L;
    private static final Logger logger = LoggerFactory.getLogger(RestartThread.class);

    /**
     * @param delay
     */
    public RestartThread(long delay) {
        this.delay = delay;
    }

    /**
     * 
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
        execute();
    }

    /**
     * @return boolean
     */
    public static boolean execute() {
        String javaHome = System.getProperty("java.home");
        String cmd = new File(javaHome, "bin/java").getAbsolutePath();
        String mainClass = Restart.class.getName();
        String processId = Self.getProcessId();
        String work = getWork();
        String banner = getBanner();
        String classPath = getClassPath();
        logger.warn("\r\n{}", banner);

        if(classPath == null) {
            logger.error("restart failed, classpath is null.");
            return false;
        }

        logger.warn("JAVA_HOME: {}", javaHome);
        logger.warn("java.class.path: {}", System.getProperty("java.class.path"));
        logger.warn("PROCESS_ID: {}", processId);
        logger.warn("WORK_DIRECTORY: {}", work);
        logger.warn("{} -classpath {} {} {} {}", cmd, classPath, mainClass, processId, work);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(OS.getCMD(cmd), "-classpath", classPath, mainClass, processId, work);
            processBuilder.directory(new File(work));
            Process process = processBuilder.start();

            new ReadThread("errout", process.getErrorStream()).start();
            new ReadThread("stdout", process.getInputStream()).start();

            int exitCode = process.waitFor();
            process.destroy();
            logger.info("exitCode: {}", exitCode);
            Thread.sleep(2000);
            return true;
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * @return String
     */
    private static String getWork() {
        String catalinaHome = System.getProperty("catalina.home");
        logger.info("catalina.home: {}", catalinaHome);

        if(catalinaHome == null) {
            try {
                return new File(".").getCanonicalPath();
            }
            catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return new File(catalinaHome, "bin").getAbsolutePath();
    }

    /**
     * @return Class<?>
     */
    protected static Class<?> getBootstrapClass() {
        try {
            return ClassUtil.getClass("org.apache.catalina.startup.Bootstrap");
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @return String
     */
    private static String getBanner() {
        StringBuilder banner = new StringBuilder();
        banner.append("================================================\r\n");
        banner.append("Restart Server\r\n");
        banner.append("================================================\r\n");
        return banner.toString();
    }

    /**
     * 仅支持独立部署
     * @return String
     */
    public static String getClassPath() {
        try {
            File jarFile = ClassUtil.getJarFile(Restart.class);

            if(jarFile == null) {
                return null;
            }

            File parent = jarFile.getParentFile();
            File[] files = parent.listFiles();
            StringBuilder buffer = new StringBuilder();

            for(File file : files) {
                String name = file.getName();

                if(name.endsWith(".jar")) {
                    buffer.append(file.getAbsolutePath());
                    buffer.append(File.pathSeparatorChar);
                }
            }

            if(buffer.length() > 0) {
                buffer.deleteCharAt(buffer.length() - 1);
            }
            return buffer.toString();
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
