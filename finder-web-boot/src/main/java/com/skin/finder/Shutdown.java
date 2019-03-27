package com.skin.finder;

import java.io.IOException;

import com.skin.finder.util.Http;

/**
 * <p>Title: Shutdown</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author Admin
 * @version 1.0
 */
public class Shutdown {
    /**
     * @param args
     */
    public static void main(String[] args) {
        String url = getShutdownURL(8081);

        try {
            Http.post(url, null);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param port
     * @return String
     */
    private static String getShutdownURL(int port) {
        StringBuilder buffer = new StringBuilder("http://localhost");

        if(port > 0 && port < 65535 && port != 80) {
            buffer.append(":");
            buffer.append(port);
        }
        buffer.append("/springboot/shutdown");
        return buffer.toString();
    }
}
