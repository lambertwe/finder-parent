/*
 * $RCSfile: LogGenerator.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: LogGenerator</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class LogGenerator extends Thread {
    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(4);
    private static final AtomicBoolean RUNNING = new AtomicBoolean(false);
    private static final Logger logger = LoggerFactory.getLogger("accessLogger");

    /**
     * @param args
     */
    public static void main(String[] args) {
        long id = 1L;
        String lineNumber = null;
        StringBuilder buffer = new StringBuilder();
        buffer.append("             ");

        for(int i = 0; i < 10; i++) {
            buffer.append("01234567890");
        }
        buffer.append("|");

        for(int i = 0; i < 30; i++) {
            lineNumber = Long.toString(id++);
            buffer.replace(0, lineNumber.length(), lineNumber);
            System.out.println(buffer.toString());
        }
    }

    /**
     * 
     */
    public static void startup() {
        THREAD_POOL.execute(new LogGenerator());
    }

    /**
     * 
     */
    public static void shutdown() {
        setRunning(false);
        ExecutorService service = THREAD_POOL;

        if(service != null) {
            Exception exception = new Exception("Stack Info");
            logger.info(exception.getMessage(), exception);

            try {
                logger.info("LogGenerator.shutdown...");
                service.shutdown();

                if(!service.awaitTermination(30000, TimeUnit.MILLISECONDS)){
                    service.shutdownNow();
                }
                logger.info("LogGenerator.shutdown");
            }
            catch(InterruptedException e) {
                service.shutdownNow();
            }
        }
    }

    /**
     * 
     */
    @Override
    public void run() {
        if(getRunning()) {
            return;
        }

        setRunning(true);

        long id = 1L;
        String lineNumber = null;
        StringBuilder buffer = new StringBuilder();
        buffer.append("             ");

        for(int i = 0; i < 60; i++) {
            buffer.append("中文中文中文中文中文");
        }
        buffer.append("|");

        while(getRunning()) {
            for(int i = 0; i < 300; i++) {
                lineNumber = Long.toString(id++);
                buffer.replace(0, lineNumber.length(), lineNumber);
                logger.debug(buffer.toString());
            }

            try {
                Thread.sleep(1000);
            }
            catch(InterruptedException e) {
                break;
            }
        }
    }

    /**
     * generate log
     */
    public static void generate() {
        String content = getStackTrace(new Exception("log generator..."));

        for(int i = 0; i < 20; i++) {
            logger.error(content);
        }
    }

    /**
     * @param file
     * @param size
     */
    public static void generate(File file, long size) {
        long count = 0L;
        long timeMillis = 0;
        OutputStream outputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss SSS");
        String content = getStackTrace(new Exception("log generator..."));

        try {
            outputStream = new FileOutputStream(file);
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            StringBuilder buffer = new StringBuilder();

            while(count < size) {
                buffer.append(dateFormat.format(new Date(timeMillis++)));
                buffer.append(" [http-nio-8088-exec-4] ERROR com.skin.finder.util.LogGenerator(46) - log generator...\r\n");
                buffer.append(content);
                byte[] bytes = buffer.toString().getBytes();
                outputStream.write(bytes, 0, bytes.length);
                buffer.setLength(0);
                count += bytes.length;
            }
            bufferedOutputStream.flush();
            outputStream.flush();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally {
            IO.close(outputStream);
        }
    }

    /**
     * @param file
     * @param size
     */
    public static void generate2(File file, long size) {
        long id = 1L;
        long count = 0L;
        OutputStream outputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            StringBuilder buffer = new StringBuilder();

            while(count < size) {
                if(id < 10L) {
                    buffer.append("00000");
                    buffer.append(id);
                }
                else if(id < 100L) {
                    buffer.append("0000");
                    buffer.append(id);
                }
                else if(id < 1000L) {
                    buffer.append("000");
                    buffer.append(id);
                }
                else if(id < 10000L) {
                    buffer.append("00");
                    buffer.append(id);
                }
                else if(id < 100000L) {
                    buffer.append("0");
                    buffer.append(id);
                }
                else {
                    buffer.append(id);
                }

                buffer.append(" 0123test901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789\r\n");
                byte[] bytes = buffer.toString().getBytes();
                outputStream.write(bytes, 0, bytes.length);
                buffer.setLength(0);
                count += bytes.length;
                id++;
            }
            bufferedOutputStream.flush();
            outputStream.flush();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally {
            IO.close(outputStream);
        }
    }

    /**
     * @param e
     * @return String
     */
    private static String getStackTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter out = new PrintWriter(stringWriter, true);
        t.printStackTrace(out);
        return stringWriter.toString();
    }

    /**
     * @param b
     */
    public static void setRunning(boolean b) {
        RUNNING.set(b);
    }

    /**
     * @return boolean
     */
    public static boolean getRunning() {
        return RUNNING.get();
    }
}
