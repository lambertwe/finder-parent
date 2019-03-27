/*
 * $RCSfile: ClusterManager.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.cluster;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.skin.finder.config.ConfigFactory;
import com.skin.finder.util.IO;
import com.skin.finder.util.Naming;
import com.skin.finder.util.StringUtil;

/**
 * <p>Title: ClusterManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ClusterManager {
    private static final String RESOURCE = "host.xml";
    private static final ReentrantLock lock = new ReentrantLock(false);
    private static final Logger logger = LoggerFactory.getLogger(ClusterManager.class);

    /**
     * cluster config
     */
    private static Cluster instance = null;

    /**
     * @return Cluster
     */
    public static Cluster getInstance() {
        lock.lock();

        if(instance == null) {
            instance = load();
        }

        Cluster cluster = instance;
        lock.unlock();
        return cluster;
    }

    /**
     * @param cluster
     */
    public static void setInstance(Cluster cluster) {
        if(cluster == null) {
            return;
        }

        lock.lock();

        if(instance != null) {
            instance.destroy();
        }

        instance = cluster;
        lock.unlock();
    }

    /**
     * @param name
     * @return Host
     */
    public static Host getHost(String name) {
        return getInstance().getHost(name);
    }

    /**
     * @param host
     * @param workspace
     * @return String
     */
    public static Workspace getWorkspace(String host, String workspace) {
        Host node = getInstance().getHost(host);
        return (node != null ? node.getWorkspace(workspace) : null);
    }

    /**
     * @param host
     * @param workspace
     * @return Workspace
     */
    public static String getCharset(String host, String workspace) {
        Host node = getInstance().getHost(host);

        if(node == null) {
            return null;
        }

        Workspace ws = node.getWorkspace(workspace);

        if(ws == null) {
            return null;
        }
        return ws.getCharset();
    }

    /**
     * @param host
     * @param workspace
     * @param defaultCharset
     * @return String
     */
    public static String getCharset(String host, String workspace, String defaultCharset) {
        String charset = getCharset(host, workspace);

        if(charset == null || charset.trim().length() < 1) {
            return defaultCharset;
        }
        return charset;
    }

    /**
     * 同步host.xml
     */
    public static void sync() {
        /**
         * 1. 将Cluster写入到host.online.xml
         * 2. 将host.online.xml发送到所有node
         */
        Cluster cluster = ClusterManager.getInstance();
        cluster.setVersion(cluster.getVersion() + 1);
        String xml = ClusterUtil.build(cluster);

        try {
            byte[] bytes = xml.getBytes("utf-8");

            /**
             * 备份原始文件
             */
            ClusterManager.backup();
            File file = getHostFile();
            IO.write(file, bytes);

            /**
             * 向集群内的所有机器发送变更过的host.xml
             */
            List<Host> hosts = cluster.getHosts();

            for(Host host : hosts) {
                String address = host.getUrl() + "?action=admin.host.accept";

                try {
                    API.send(address, bytes);
                }
                catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 存储本地内存中的集群状态
     * @throws IOException
     */
    public static void restore() throws IOException {
        Cluster cluster = ClusterManager.getInstance();
        String xml = ClusterUtil.build(cluster);
        restore(xml.getBytes("utf-8"), false);
    }

    /**
     * @param bytes
     * @param reload
     * @throws IOException
     */
    public static void restore(byte[] bytes, boolean reload) throws IOException {
        ClusterManager.backup();
        File file = getHostFile();
        IO.write(file, bytes);

        if(reload) {
            ClusterManager.reload();
        }
    }

    /**
     * @throws IOException
     */
    public static void backup() throws IOException {
        File file = getHostFile();
        File parent = file.getParentFile();
        File backup = new File(parent, "host.default.xml");

        if(!backup.exists()) {
            IO.copy(file, backup, true);
        }
    }

    /**
     * @param bytes
     * @throws IOException
     */
    public static void save(byte[] bytes) throws IOException {
        ClusterManager.backup();
        File file = getHostFile();
        IO.write(file, bytes);
    }

    /**
     * 向指定的主机发送host.xml
     * @param host
     * @return boolean
     * @throws IOException
     */
    public static boolean send(Host host) throws IOException {
        Cluster cluster = ClusterManager.getInstance();
        String url = host.getUrl() + "?action=admin.host.accept";
        String xml = ClusterUtil.build(cluster);
        return API.send(url, xml.getBytes("utf-8"));
    }

    /**
     * @return File
     * @throws IOException
     */
    public static File getHostFile() throws IOException {
        URL url = ConfigFactory.getResource(RESOURCE);
        return IO.getFile(url);
    }

    /**
     * reload host.xml
     * @return boolean
     */
    public static boolean reload() {
        Cluster cluster = load();

        if(cluster == null) {
            logger.warn("reload cluster failed!");
            return false;
        }
        setInstance(cluster);
        return true;
    }

    /**
     * @return Cluster
     */
    private static Cluster load() {
        InputStream inputStream = ConfigFactory.getInputStream(RESOURCE);

        if(inputStream != null) {
            try {
                return load(inputStream, "utf-8");
            }
            catch(Exception e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
            finally {
                try {
                    inputStream.close();
                }
                catch(IOException e) {
                }
            }
        }
        return null;
    }

    /**
     * @param inputStream
     * @param charset
     * @return Cluster
     * @throws Exception
     */
    public static Cluster load(InputStream inputStream, String charset) throws Exception {
        BufferedInputStream buffer = getUTF8Stream(inputStream);
        return ClusterManager.load(new InputStreamReader(buffer, charset));
    }

    /**
     * @param reader
     * @return Cluster
     * @throws Exception
     */
    public static Cluster load(Reader reader) throws Exception {
        Cluster cluster = new Cluster();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new InputSource(reader));
        Element element = document.getDocumentElement();
        NodeList childNodes = element.getChildNodes();
        Long version = getLongAttribute(element, "version", 1);
        logger.debug("cluster.version: {}", version);

        if(version < 1L) {
            throw new IllegalArgumentException("bad version: " + version);
        }

        int orderNum = 1;
        int length = childNodes.getLength();
        cluster.setVersion(version);

        for(int i = 0; i < length; i++) {
            Node node = childNodes.item(i);
            int nodeType = node.getNodeType();

            if(nodeType == Node.ELEMENT_NODE) {
                String nodeName = node.getNodeName();

                if(nodeName.equals("host")) {
                    Host host = getHost(node);

                    if(host != null) {
                        host.setOrderNum(orderNum);
                        cluster.add(host);
                        orderNum++;
                    }
                }
            }
        }
        return cluster;
    }

    /**
     * @param inputStream
     * @return BufferedInputStream
     * @throws IOException
     */
    private static BufferedInputStream getUTF8Stream(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[3];
        BufferedInputStream stream = new BufferedInputStream(inputStream);
        stream.mark(3);
        stream.read(bytes, 0, 3);

        if((bytes[0] & 0xFF) == 0xEF && (bytes[1] & 0xFF) == 0xBB && (bytes[2] & 0xFF) == 0xBF) {
            return stream;
        }
        else {
            stream.reset();
            return stream;
        }
    }

    /**
     * @param node
     * @return Host
     * @throws Exception
     */
    private static Host getHost(Node node) throws Exception {
        Host host = new Host();
        host.setName(getAttribute(node, "name", null));
        host.setDisplayName(getAttribute(node, "displayName", null));
        host.setUrl(getAttribute(node, "url", null));

        if(!Naming.check(host.getName())) {
            throw new IllegalArgumentException("bad host name: " + host.getName());
        }

        if(StringUtil.isBlank(host.getUrl())) {
            throw new NullPointerException("url must be not null");
        }

        NodeList childNodes = node.getChildNodes();

        if(childNodes.getLength() < 1) {
            return host;
        }

        int orderNum = 1;
        int length = childNodes.getLength();

        for(int i = 0; i < length; i++) {
            Node item = childNodes.item(i);

            if(item.getNodeType() == Node.ELEMENT_NODE) {
                String nodeName = item.getNodeName();

                if(nodeName.equals("workspace")) {
                    Workspace workspace = getWorkspace(item);
                    workspace.setOrderNum(orderNum);
                    host.add(workspace);
                    orderNum++;
                }
            }
        }
        return host;
    }

    /**
     * @param node
     * @return Workspace
     */
    private static Workspace getWorkspace(Node node) {
        Workspace workspace = new Workspace();
        workspace.setName(getAttribute(node, "name", null));
        workspace.setDisplayName(getAttribute(node, "displayName", null));
        workspace.setWork(getAttribute(node, "work", null));
        workspace.setCharset(getAttribute(node, "charset", null));
        workspace.setReadonly(getBooleanAttribute(node, "readonly", false));

        if(!Naming.check(workspace.getName())) {
            throw new IllegalArgumentException("bad workspace name: " + workspace.getName());
        }

        if(StringUtil.isBlank(workspace.getWork())) {
            throw new NullPointerException("url must be not null");
        }
        return workspace;
    }

    /**
     * @param node
     * @param name
     * @param defaultValue
     * @return String
     */
    private static String getAttribute(Node node, String name, String defaultValue) {
        NamedNodeMap attributes = node.getAttributes();

        if(attributes != null) {
            Node item = attributes.getNamedItem(name);

            if(item != null) {
                return item.getTextContent();
            }
        }
        return defaultValue;
    }

    /**
     * @param node
     * @param name
     * @param defaultValue
     * @return int
     */
    protected static int getIntAttribute(Node node, String name, int defaultValue) {
        String value = getAttribute(node, name, null);

        if(value != null) {
            try {
                return Integer.parseInt(value);
            }
            catch(NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    /**
     * @param node
     * @param name
     * @param defaultValue
     * @return int
     */
    protected static long getLongAttribute(Node node, String name, long defaultValue) {
        String value = getAttribute(node, name, null);

        if(value != null) {
            try {
                return Long.parseLong(value);
            }
            catch(NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    /**
     * @param node
     * @param name
     * @param defaultValue
     * @return boolean
     */
    private static boolean getBooleanAttribute(Node node, String name, boolean defaultValue) {
        String value = getAttribute(node, name, null);

        if(value != null) {
            return value.equals("true");
        }
        return defaultValue;
    }

    /**
     * @param host
     * @return String
     */
    protected static String getDomain(String host, int port, String path) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("http://");
        buffer.append(host);

        if(port > 0 && port != 80) {
            buffer.append(port);
        }
        buffer.append(path);
        return buffer.toString();
    }
}
