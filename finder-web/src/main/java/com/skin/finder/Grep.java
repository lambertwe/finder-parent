/*
 * $RCSfile: Grep.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Pattern;

/**
 * <p>Title: Grep</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Grep {
    /**
     * grep
     */
    private Grep() {
    }

    /**
     * @param raf
     * @param keyword
     * @param regexp
     * @param position
     * @param rows
     * @param charset
     * @return FileRange
     * @throws IOException
     */
    public static FileRange find(RandomAccessFile raf, String keyword, boolean regexp, long position, int rows, String charset) throws IOException {
        long start = position;
        long length = raf.length();

        if((start + 1) >= length) {
            FileRange range = new FileRange();
            range.setStart(length);
            range.setEnd(length);
            range.setCount(0L);
            range.setLength(length);
            range.setRows(0);
            return range;
        }

        if(start < 0) {
            start = 0;
        }

        byte LF = 0x0A;
        int readBytes = 0;
        int bufferSize = (int)(Math.min(length - start, 8L * 1024L));
        byte[] buffer = new byte[bufferSize];
        raf.seek(start);

        if(start > 0) {
            boolean flag = false;

            while((readBytes = raf.read(buffer, 0, bufferSize)) > 0) {
                for(int i = 0; i < readBytes; i++) {
                    if(buffer[i] == LF) {
                        start = start + i + 1;
                        flag = true;
                        break;
                    }
                }

                if(flag) {
                    break;
                }
                else {
                    start += readBytes;
                }
            }

            if(flag) {
                raf.seek(start);
            }
            else {
                FileRange range = new FileRange();
                range.setStart(length - 1);
                range.setEnd(length - 1);
                range.setLength(length);
                range.setRows(0);
                return range;
            }
        }

        int count = 0;
        int dataSize = 0;
        byte[] bytes = null;
        boolean match = false;
        long endPos = start;
        long startTime = System.currentTimeMillis();
        ByteArrayOutputStream tmp = new ByteArrayOutputStream(8192);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(8192);
        Pattern pattern = (regexp ? Pattern.compile(keyword) : null);

        while((bytes = readLine(raf, buffer, tmp)) != null) {
            String line = new String(bytes, 0, bytes.length, charset);
            endPos += bytes.length;

            if(pattern != null) {
                match = pattern.matcher(line).find();
            }
            else {
                match = (line.indexOf(keyword) > -1);
            }

            if(match) {
                bos.write(bytes, 0, bytes.length);
                dataSize += bytes.length;
                count++;
            }

            tmp.reset();

            if(count >= rows || dataSize >= Less.MAX_SIZE || (System.currentTimeMillis() - startTime) > 10L * 1000) {
                break;
            }
        }

        byte[] result = bos.toByteArray();
        FileRange range = new FileRange();
        range.setStart(start);
        range.setEnd(endPos);
        range.setCount(dataSize);
        range.setLength(length);
        range.setRows(count);
        range.setBuffer(result);
        return range;
    }

    /**
     * @param bufferedInputStream
     * @param byteArrayOutputStream
     * @return byte[]
     * @throws IOException
     */
    private static byte[] readLine(RandomAccessFile raf, byte[] buffer, ByteArrayOutputStream bos) throws IOException {
        int length = 0;
        int bufferSize = buffer.length;
        long position = raf.getFilePointer();

        while((length = raf.read(buffer, 0, bufferSize)) > -1) {
            int k = indexOf(buffer, 0x0A, length);

            if(k > -1) {
                raf.seek(position + bos.size() + k + 1);
                bos.write(buffer, 0, k + 1);
                break;
            }
            else {
                bos.write(buffer, 0, length);
            }
        }

        if(length < 0 && bos.size() < 1) {
            return null;
        }
        return bos.toByteArray();
    }

    /**
     * @param bytes
     * @param b
     * @return int
     */
    private static int indexOf(byte[] bytes, int b, int length) {
        for(int i = 0; i < length; i++) {
            if(bytes[i] == b) {
                return i;
            }
        }
        return -1;
    }
}
