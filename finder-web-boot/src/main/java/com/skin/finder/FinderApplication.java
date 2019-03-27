/*
 * $RCSfile: FinderApplication.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.ClassPathResource;

/**
 * <p>Title: FinderApplication</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
@SpringBootApplication
@RestController
public class FinderApplication implements InitializingBean {
    @Resource
    private ServletContext servletContext;
    private com.skin.finder.web.ActionDispatcher dispatcher;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(FinderApplication.class);
        springApplication.setBanner(new ResourceBanner(new ClassPathResource("banner.txt")));
        springApplication.run(args);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.dispatcher = new com.skin.finder.web.ActionDispatcher();
        this.dispatcher.setPackages(new String[]{"com.skin.finder.servlet", "com.skin.finder.admin.servlet"});
        this.dispatcher.init(this.servletContext);
    }

    /**
     * 凡是集成到其他MVC框架中的, finder的集群模式可能会失效。
     * 要在集成模式下安全的使用finder的集群模式，请使用第一种方式集成，即配置servlet的方式。
     * 并且要将servlet配置到最前面，确保在其他mvc框架之前执行。
     */
    @RequestMapping(value = "/finder")
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String loginURL = com.skin.finder.filter.SessionFilter.getLoginURL(request);

        /**
         * 若要取消鉴权, 放开此处的代码, 注入管理员帐号
         * 所有用户使用同一账号登录, 该账号必须是有效的用户帐号
         * LoginServlet.login(request, response, "admin");
         */

        if(com.skin.finder.filter.SessionFilter.check(request, response, loginURL)) {
            this.dispatcher.service(request, response);
        }
    }
}
