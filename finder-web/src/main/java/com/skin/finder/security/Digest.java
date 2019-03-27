/*
 * $RCSfile: Digest.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * <p>Title: Digest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @version 1.0
 */
public class Digest {
    /**
     * @param input
     * @return byte[]
     */
    public static byte[] sha1(byte[] input) {
        return digest("SHA-1", input, null);
    }

    /**
     * @param input
     * @param salt
     * @return byte[]
     */
    public static byte[] sha1(byte[] input, byte[] salt) {
        return digest("SHA-1", input, salt);
    }

    /**
     * @param input
     * @param salt
     * @return byte[]
     */
    public static byte[] md5(byte[] input, byte[] salt) {
        return digest("MD5", input, salt);
    }

    /**
     * @param input
     * @param algorithm
     * @param salt
     * @return byte[]
     */
    public static byte[] digest(String algorithm, byte[] input, byte[] salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);

            if(salt != null) {
                digest.update(salt);
            }
            return digest.digest(input);
        }
        catch(GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param algorithm
     * @param file
     * @return byte[]
     * @throws IOException
     */
    public static byte[] digest(String algorithm, File file) throws IOException {
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);
            return Digest.digest(algorithm, inputStream);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                }
            }
        }
        return null;
    }

    /**
     * @param algorithm
     * @param inputStream
     * @return byte[]
     * @throws IOException
     */
    public static byte[] digest(String algorithm, InputStream inputStream) throws IOException {
        try {
            int length = 0;
            int bufferSize = 8192;
            byte[] buffer = new byte[bufferSize];
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

            while((length = inputStream.read(buffer, 0, bufferSize)) > 0) {
                messageDigest.update(buffer, 0, length);
            }
            return messageDigest.digest();
        }
        catch(GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param size
     * @return byte[]
     */
    public static byte[] salt(int size) {
        byte[] bytes = new byte[size];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }
}
