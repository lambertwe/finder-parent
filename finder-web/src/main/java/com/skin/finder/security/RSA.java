/*
 * $RCSfile: RSA.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2005 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.security;

import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;

import javax.crypto.Cipher;

/**
 * <p>Title: RSA</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class RSA {
    private static final String ALGORITHM = "RSA";

    /**
     * RSA最大加密明文大小
     */
    public static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    public static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * @return String
     */
    public static String getProviders() {
        StringBuilder buffer = new StringBuilder();

        for(Provider provider : Security.getProviders()) {
            buffer.append(provider);
            buffer.append("\r\n");
        }
        return buffer.toString();
    }

    /**
     * @param data
     * @param publicKey
     * @return byte[]
     */
    public static byte[] encrypt(byte[] data, PublicKey publicKey) {
        if(publicKey == null) {
            throw new NullPointerException("publicKey must be not null.");
        }

        try {
            int i = 0;
            int offSet = 0;
            byte[] packData = null;
            int length = data.length;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            while(length - offSet > 0) {
                if(length - offSet > MAX_ENCRYPT_BLOCK) {
                    packData = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                }
                else {
                    packData = cipher.doFinal(data, offSet, length - offSet);
                }

                bos.write(packData, 0, packData.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            return bos.toByteArray();
        }
        catch(Exception e) {
            throw new RuntimeException("rsa.encrypt.error", e);
        }
    }

    /**
     * @param data
     * @param privateKey
     * @return byte[]
     */
    public static byte[] decrypt(byte[] data, PrivateKey privateKey) {
        if(privateKey == null) {
            throw new NullPointerException("privateKey must be not null.");
        }

        try {

            int i = 0;
            int offSet = 0;
            int length = data.length;
            byte[] packData = null;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            while(length - offSet > 0) {
                if(length - offSet > MAX_DECRYPT_BLOCK) {
                    packData = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
                }
                else {
                    packData = cipher.doFinal(data, offSet, length - offSet);
                }

                bos.write(packData, 0, packData.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            return bos.toByteArray();
        }
        catch(Exception e) {
            throw new RuntimeException("rsa.decrypt.error", e);
        }
    }
}
