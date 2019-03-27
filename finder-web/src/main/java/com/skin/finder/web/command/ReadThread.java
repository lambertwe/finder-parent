package com.skin.finder.web.command;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.util.IO;

/**
 * <p>Title: ReadThread</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author ReadThread
 * @version 1.0
 */
public class ReadThread extends Thread {
    private String name;
    private InputStream inputStream;
    private static final Logger logger = LoggerFactory.getLogger(ReadThread.class);

    /**
     * @param name
     * @param inputStream
     */
    public ReadThread(String name, InputStream inputStream) {
        this.name = name;
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        read(this.inputStream, this.name, "gbk");
    }

    /**
     * @param inputStream
     * @param encoding
     */
    protected static void read(InputStream inputStream, String prefix, String encoding) {
        String line = null;
        BufferedReader buffer = null;

        try {
            buffer = new BufferedReader(new InputStreamReader(inputStream, encoding));

            while((line = buffer.readLine()) != null) {
                logger.info("{}: {}", prefix, line);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            IO.close(buffer);
            IO.close(inputStream);
        }
        logger.info("{}: end.", prefix, line);
    }
}
