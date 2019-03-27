/*
 * $RCSfile: SystemInfo.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

/**
 * <p>Title: SystemInfo</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class SystemInfo {
    private String vmName;
    private String vmVendor;
    private String vmVersion;
    private String runtimeName;
    private String runtimeVersion;
    private String osName;
    private String osVersion;
    private String cpu;
    private long maxMemory = 0L;
    private long totalMemory = 0L;
    private long freeMemory = 0L;

    /**
     *
     */
    public SystemInfo() {
        this.vmName = System.getProperty("java.vm.name", "");
        this.vmVendor = System.getProperty("java.vm.vendor");
        this.vmVersion = System.getProperty("java.vm.version");
        this.runtimeName = System.getProperty("java.runtime.name");
        this.runtimeVersion = System.getProperty("java.runtime.version");
        this.osName = System.getProperty("os.name");
        this.osVersion = System.getProperty("os.version");
        this.cpu = System.getProperty("sun.cpu.isalist");

        Runtime runtime = Runtime.getRuntime();
        this.maxMemory = runtime.maxMemory();
        this.totalMemory = runtime.totalMemory();
        this.freeMemory = runtime.freeMemory();
    }

    /**
     * @return the vmName
     */
    public String getVmName() {
        return this.vmName;
    }

    /**
     * @param vmName the vmName to set
     */
    public void setVmName(String vmName) {
        this.vmName = vmName;
    }

    /**
     * @return the vmVendor
     */
    public String getVmVendor() {
        return this.vmVendor;
    }

    /**
     * @param vmVendor the vmVendor to set
     */
    public void setVmVendor(String vmVendor) {
        this.vmVendor = vmVendor;
    }

    /**
     * @return the vmVersion
     */
    public String getVmVersion() {
        return this.vmVersion;
    }

    /**
     * @param vmVersion the vmVersion to set
     */
    public void setVmVersion(String vmVersion) {
        this.vmVersion = vmVersion;
    }

    /**
     * @return the runtimeName
     */
    public String getRuntimeName() {
        return this.runtimeName;
    }

    /**
     * @param runtimeName the runtimeName to set
     */
    public void setRuntimeName(String runtimeName) {
        this.runtimeName = runtimeName;
    }

    /**
     * @return the runtimeVersion
     */
    public String getRuntimeVersion() {
        return this.runtimeVersion;
    }

    /**
     * @param runtimeVersion the runtimeVersion to set
     */
    public void setRuntimeVersion(String runtimeVersion) {
        this.runtimeVersion = runtimeVersion;
    }

    /**
     * @return the osName
     */
    public String getOsName() {
        return this.osName;
    }

    /**
     * @param osName the osName to set
     */
    public void setOsName(String osName) {
        this.osName = osName;
    }

    /**
     * @return the osVersion
     */
    public String getOsVersion() {
        return this.osVersion;
    }

    /**
     * @param osVersion the osVersion to set
     */
    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    /**
     * @return the cpu
     */
    public String getCpu() {
        return this.cpu;
    }

    /**
     * @param cpu the cpu to set
     */
    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    /**
     * @return the maxMemory
     */
    public long getMaxMemory() {
        return this.maxMemory;
    }

    /**
     * @param maxMemory the maxMemory to set
     */
    public void setMaxMemory(long maxMemory) {
        this.maxMemory = maxMemory;
    }

    /**
     * @return the totalMemory
     */
    public long getTotalMemory() {
        return this.totalMemory;
    }

    /**
     * @param totalMemory the totalMemory to set
     */
    public void setTotalMemory(long totalMemory) {
        this.totalMemory = totalMemory;
    }

    /**
     * @return the freeMemory
     */
    public long getFreeMemory() {
        return this.freeMemory;
    }

    /**
     * @param freeMemory the freeMemory to set
     */
    public void setFreeMemory(long freeMemory) {
        this.freeMemory = freeMemory;
    }
}
