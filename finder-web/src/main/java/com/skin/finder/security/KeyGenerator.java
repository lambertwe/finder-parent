/*
 * $RCSfile: KeyGenerator.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2005 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.util.Base64;

/**
 * <p>Title: KeyGenerator</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class KeyGenerator {
    private static final Logger logger = LoggerFactory.getLogger(KeyGenerator.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        generate();
    }

    /**
     *
     */
    public static void generate() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            System.out.println("public static final String PUBLIC_KEY = \"" + new String(Base64.encode(publicKey.getEncoded())) + "\";");
            System.out.println("public static final String PRIVATE_KEY = \"" + new String(Base64.encode(privateKey.getEncoded())) + "\";");
        }
        catch(Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    /**
     * @param algorithm
     * @param keySize
     * @return KeyPair
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair getKeyPair(String algorithm, int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * @param keyPair
     * @return String
     */
    public static String getPublicKey(KeyPair keyPair) {
        PublicKey publicKey = keyPair.getPublic();
        return new String(Base64.encode(publicKey.getEncoded()));
    }

    /**
     * @param keyPair
     * @return String
     */
    public static String getPrivateKey(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        return new String(Base64.encode(privateKey.getEncoded()));
    }
}
