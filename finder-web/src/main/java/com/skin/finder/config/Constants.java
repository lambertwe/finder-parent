/*
 * $RCSfile: Constants.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.config;

/**
 * <p>Title: Constants</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Constants {
    /**
     * 应用ID
     */
    public static final long APP_ID = 1L;

    /**
     * 请求头安全Key
     */
    public static final String HTTP_SECURITY_KEY = "Security-Key";

    /**
     * demo | test | prep | prod
     */
    public static final String ENV_NAME = "finder.env.name";

    /**
     * 集群中当前机器的名称, 全局唯一
     */
    public static final String CLUSTER_NODE_NAME = "finder.cluster.node.name";

    /**
     * 集群中master机器的名称
     */
    public static final String CLUSTER_MASTER_NAME = "finder.cluster.master.name";

    /**
     * 集群间通信安全KEY
     */
    public static final String CLUSTER_SECURITY_KEY = "finder.cluster.security.key";

    /**
     * 系统超级管理员帐号
     */
    public static final String CLUSTER_SECURITY_ROOT = "finder.security.root";

    /**
     * 用户登录加密RSA公钥
     */
    public static final String PUBLIC_KEY = "finder.security.public-key";

    /**
     * 用户登录加密RSA私钥
     */
    public static final String PRIVATE_KEY = "finder.security.private-key";

    /**
     * 用户会话md5 key
     */
    public static final String SESSION_KEY = "finder.session.key";

    /**
     * 用户会话有效期
     */
    public static final String SESSION_NAME = "finder.session.name";

    /**
     * 用户会话有效期
     */
    public static final String SESSION_TIMEOUT = "finder.session.timeout";

    /**
     * 文本文件类型
     */
    public static final String TEXT_TYPE = "finder.text.type";

    /**
     * 文件列表页面显示的按钮
     */
    public static final String DISPLAY_OPERATE_BUTTON = "finder.display.operate-button";

    /**
     * 客户端上传大文件的分片大小
     */
    public static final String UPLOAD_PART_SIZE = "finder.upload.part-size";

    /**
     * 版本检查
     */
    public static final String UPDATE_CHECK = "finder.update.check";

    /**
     * 演示账号用户
     */
    public static final String DEMO_USERNAME = "finder.demo.userName";

    /**
     * 演示账号密码
     */
    public static final String DEMO_PASSWORD = "finder.demo.password";

    /**
     * 统计代码
     */
    public static final String ACCESS_CODE = "finder.access.code";

    /**
     * 配置文件版本号
     */
    public static final String CONF_VERSION = "finder.conf.version";

    /**
     * 缺省的公钥
     */
    public static final String DEFAULT_PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJGw4fUyFA6IQsYWBwVzYzGHX38Hq48zYo2vuL31oXnpsPCfVzDFjYCIvC2FZexJ0mxvz337qCK0G6yhc2euifMCAwEAAQ==";

    /**
     * 缺省的私钥
     */
    public static final String DEFAULT_PRIVATE_KEY = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAkbDh9TIUDohCxhYHBXNjMYdffwerjzNija+4vfWheemw8J9XMMWNgIi8LYVl7EnSbG/PffuoIrQbrKFzZ66J8wIDAQABAkAgPr70Dt5jjQMCZpOtQCmfJ7AaJG9zJVNVuqOv33Ka0adJ0fOJKOveHcb8NsrG+g4AnVreitxBeVluMsc2lLwRAiEA9G0QaqN7r1mjXEUh3XymGCM0EGpQW2RlGAJXHiaL1/sCIQCYlvNORd9NVywp0D+wFHwoptH3hTpm8kZHmsFndkKcaQIhAMDjCJ9+3+5i6J26GJ127oQB0+ZYX42fzn+B6Unr1VPdAiB5oVmwOuIUtDfv3J/nq2yMuu0DcJ9tEBIVYbZo0kumgQIgBHbaq0ZFzwOIoATOVFam+hhToc9MLfbaaYLlq9QBYJY=";
}
