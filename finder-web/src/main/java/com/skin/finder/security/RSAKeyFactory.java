/*
 * $RCSfile: RSAKeyFactory.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2005 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.security;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.util.Base64;

/**
 * <p>Title: RSAKeyFactory</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class RSAKeyFactory {
    private static final Logger logger = LoggerFactory.getLogger(RSAKeyFactory.class);

    /**
     * @param key
     * @return PublicKey
     */
    public static PublicKey getPublicKey(String key) {
        if(key == null || key.length() < 1) {
            return null;
        }

        try {
            byte[] bytes = Base64.decode(key.getBytes());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(bytes);
            return keyFactory.generatePublic(x509EncodedKeySpec);
        }
        catch(NoSuchAlgorithmException e) {
            logger.warn(e.getMessage(), e);
        }
        catch(InvalidKeySpecException e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @param bytes
     * @return PublicKey
     */
    public static PublicKey getPublicKey(byte[] bytes) {
        if(bytes == null || bytes.length < 1) {
            return null;
        }

        PublicKey publicKey = null;

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(bytes);
            publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        }
        catch(NoSuchAlgorithmException e) {
            logger.warn(e.getMessage(), e);
        }
        catch(InvalidKeySpecException e) {
            logger.warn(e.getMessage(), e);
        }
        return publicKey;
    }

    /**
     * @param key
     * @return PrivateKey
     */
    public static PrivateKey getPrivateKey(String key) {
        if(key == null || key.length() < 1) {
            return null;
        }

        try {
            byte[] bytes = Base64.decode(key.getBytes());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(bytes);
            return keyFactory.generatePrivate(pKCS8EncodedKeySpec);
        }
        catch(NoSuchAlgorithmException e) {
            logger.warn(e.getMessage(), e);
        }
        catch(InvalidKeySpecException e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @param bytes
     * @return PrivateKey
     */
    public static PrivateKey getPrivateKey(byte[] bytes) {
        if(bytes == null || bytes.length < 1) {
            return null;
        }

        PrivateKey privateKey = null;

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(bytes);
            privateKey = keyFactory.generatePrivate(pKCS8EncodedKeySpec);
        }
        catch(NoSuchAlgorithmException e) {
            logger.warn(e.getMessage(), e);
        }
        catch(InvalidKeySpecException e) {
            logger.warn(e.getMessage(), e);
        }
        return privateKey;
    }
}
