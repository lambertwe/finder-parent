/*
 * $RCSfile: Naming.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

/**
 * <p>Title: Naming</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Naming {
    /**
     * 命名规则: 
     * 1. 只能是字母或者数字开头
     * 2. 可以包含下列字符: [a-z], [A-Z], [0-9], [.-_:@]
     * 例如下列名称都是合法的: 
     * 192.168.1.1:8080, 192.168.1.1_8080, 192.168.1.1-8080, 192.168.1.1@8080
     * @param name
     * @return boolean
     */
    public static boolean check(String name) {
        if(name == null || name.length() < 1) {
            return false;
        }

        if(!Naming.isIdentifierStart(name.charAt(0))) {
            return false;
        }

        int length = name.length();

        for(int i = 1; i < length; i++) {
            if(!isIdentifierPart(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param c
     * @return boolean
     */
    public static boolean isIdentifierStart(char c) {
        return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'));
    }

    /**
     * @param c
     * @return boolean
     */
    public static boolean isIdentifierPart(char c) {
        switch(c) {
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 't':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '.':
            case '_':
            case '-':
            case '@':
            case ':':
                return true;
            default:
                return false;
        }
    }
}
