/*
 * $RCSfile: Version.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.security.CodeSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>Title: Version</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Version {
    private String name;
    private int majorVersion;
    private int minorVersion;
    private String milestone;
    private List<String> features;
    private List<String> dependencies;
    private String developer;
    private Date buildTime;
    private Date updateTime;
    private String signature;
    private static final Version instance = load();

    /**
     * @return Version
     */
    public static Version getInstance() {
        return instance;
    }

    /**
     * @return Version
     */
    protected static Version load() {
        Version version = new Version();
        version.setName("finder");
        version.setMajorVersion(2);
        version.setMinorVersion(33);
        version.setDeveloper("http://www.finderweb.net");
        return version;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the majorVersion
     */
    public int getMajorVersion() {
        return this.majorVersion;
    }

    /**
     * @param majorVersion the majorVersion to set
     */
    public void setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
    }

    /**
     * @return the minorVersion
     */
    public int getMinorVersion() {
        return this.minorVersion;
    }

    /**
     * @param minorVersion the minorVersion to set
     */
    public void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }

    /**
     * @return the milestone
     */
    public String getMilestone() {
        return this.milestone;
    }

    /**
     * @param milestone the milestone to set
     */
    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    /**
     * @return the features
     */
    public List<String> getFeatures() {
        return this.features;
    }

    /**
     * @param features the features to set
     */
    public void setFeatures(List<String> features) {
        this.features = features;
    }

    /**
     * @param feature
     */
    public void addFeature(String feature) {
        if(feature != null && feature.trim().length() > 0) {
            if(this.features == null) {
                this.features = new ArrayList<String>();
            }

            this.features.add(feature.trim());
        }
    }

    /**
     * @return the dependencies
     */
    public List<String> getDependencies() {
        return this.dependencies;
    }

    /**
     * @param dependencies the dependencies to set
     */
    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }

    /**
     * @param dependency
     */
    public void addDependency(String dependency) {
        if(dependency != null && dependency.trim().length() > 0) {
            if(this.dependencies == null) {
                this.dependencies = new ArrayList<String>();
            }

            this.dependencies.add(dependency.trim());
        }
    }

    /**
     * @return the developer
     */
    public String getDeveloper() {
        return this.developer;
    }

    /**
     * @param developer the developer to set
     */
    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    /**
     * @return the updateTime
     */
    public Date getUpdateTime() {
        return this.updateTime;
    }

    /**
     * @return the buildTime
     */
    public Date getBuildTime() {
        return this.buildTime;
    }

    /**
     * @param buildTime the buildTime to set
     */
    public void setBuildTime(Date buildTime) {
        this.buildTime = buildTime;
    }

    /**
     * @param updateTime the updateTime to set
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return the signature
     */
    public String getSignature() {
        return this.signature;
    }

    /**
     * @param signature the signature to set
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * @param object
     * @return boolean
     */
    @Override
    public boolean equals(Object object) {
        if(object == null) {
            return false;
        }

        if(this == object) {
            return true;
        }

        if(!(object instanceof Version)) {
            return false;
        }

        Version version = (Version)object;

        if(this.name.equals(version.getName()) == false) {
            return false;
        }

        if(this.majorVersion != version.getMajorVersion()) {
            return false;
        }

        if(this.minorVersion != version.getMinorVersion()) {
            return false;
        }

        if(this.developer.equals(version.getDeveloper()) == false) {
            return false;
        }

        if(this.features != null && version.getFeatures() != null) {
            List<String> list1 = this.features;
            List<String> list2 = version.getFeatures();

            if(list1.size() != list2.size()) {
                return false;
            }

            for(int i = 0, size = list1.size(); i < size; i++) {
                String f1 = list1.get(i);
                String f2 = list2.get(i);

                if(f1.equals(f2) == false) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @return String
     */
    public String getVersionName() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.getName());
        buffer.append("-");
        buffer.append(this.getVersion());

        String milestone = this.getMilestone();

        if(milestone != null) {
            milestone = milestone.trim();

            if(milestone.length() > 0) {
                buffer.append("-");
                buffer.append(milestone);
            }
        }
        return buffer.toString();
    }

    /**
     * @return String
     */
    public String getVersion() {
        int level = 3;
        int mod = this.getMinorVersion();
        int[] temp = new int[level];
        StringBuilder buffer = new StringBuilder();

        for(int i = 0; i < level; i++) {
            temp[level - 1 - i] = mod % 10;
            mod = mod / 10;
        }

        temp[0] = this.getMajorVersion() + (mod * 10) + temp[0];

        for(int i = 0; i < level; i++) {
            buffer.append(temp[i]);
            buffer.append(".");
        }

        if(buffer.length() > 0) {
            buffer.deleteCharAt(buffer.length() - 1);
        }
        return buffer.toString();
    }

    /**
     * @param type
     * @param defaultVersion
     * @return String
     */
    public static String getVersion(Class<?> type, String defaultVersion) {
        try {
            /**
             * 首先查找MANIFEST.MF规范中的版本号
             */
            String version = type.getPackage().getImplementationVersion();

            if(StringUtil.isBlank(version)) {
                version = type.getPackage().getSpecificationVersion();
            }

            if(StringUtil.isBlank(version)) {
                /**
                 * 如果规范中没有版本号，基于jar包名获取版本号
                 */
                CodeSource codeSource = type.getProtectionDomain().getCodeSource();

                if(codeSource != null) {
                    String file = codeSource.getLocation().getFile();

                    if(StringUtil.notBlank(file) && file.endsWith(".jar")) {
                        file = file.substring(0, file.length() - 4);
                        int i = file.lastIndexOf('/');

                        if (i >= 0) {
                            file = file.substring(i + 1);
                        }

                        i = file.indexOf("-");

                        if(i >= 0) {
                            file = file.substring(i + 1);
                        }
                        version = getVersion(file);
                    }
                }
            }
            return StringUtil.isBlank(version) ? defaultVersion : version;
        }
        catch (Throwable e) {
            return defaultVersion;
        }
    }

    /**
     * @param name
     * @return String
     */
    private static String getVersion(String name) {
        int i = 0;
        int length = name.length();

        while(i < length) {
            if(!Character.isDigit(name.charAt(i))) {
                i = name.indexOf("-", i);

                if(i > -1) {
                    i++;
                }
                else {
                    return "";
                }
            }
            else {
                return name.substring(i);
            }
        }
        return name;
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return this.toString("&");
    }

    /**
     * @param sperator
     * @return String
     */
    public String toString(String sperator) {
        StringBuilder buffer = new StringBuilder();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        buffer.append("name=").append(this.name).append(sperator);
        buffer.append("majorVersion=").append(this.majorVersion).append(sperator);
        buffer.append("minorVersion=").append(this.minorVersion).append(sperator);
        buffer.append("milestone=").append(this.milestone).append(sperator);

        if(this.features != null && !this.features.isEmpty()) {
            for(String feature : this.features) {
                buffer.append(feature).append(sperator);
            }
        }

        if(this.dependencies != null && !this.dependencies.isEmpty()) {
            for(String dependency : this.dependencies) {
                buffer.append(dependency).append(sperator);
            }
        }

        buffer.append("developer=").append(this.developer).append(sperator);

        if(this.buildTime != null) {
            buffer.append("buildTime=").append(dateFormat.format(this.buildTime)).append(sperator);
        }
        else {
            buffer.append("buildTime=").append(sperator);
        }

        if(this.updateTime != null) {
            buffer.append("updateTime=").append(dateFormat.format(this.updateTime)).append(sperator);
        }
        else {
            buffer.append("updateTime=").append(sperator);
        }
        buffer.append("signature=").append(this.signature).append(sperator);
        return buffer.toString();
    }
}
