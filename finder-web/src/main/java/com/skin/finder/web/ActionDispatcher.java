/*
 * $RCSfile: ActionDispatcher.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.util.Copyright;

/**
 * <p>
 * Title: ActionDispatcher
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * @author xuesong.net
 * @version 1.0
 */
public class ActionDispatcher {
	private String[]			packages;
	private ClassLoader			classLoader;
	private ServletContext		servletContext;
	private ActionContext		actionContext;
	private static final String	SERVLET_CONTEXT	= "servletContext";
	private static final Logger	logger			= LoggerFactory.getLogger(ActionDispatcher.class);

	/**
	 * default
	 */
	public ActionDispatcher() {
	}

	/**
	 * @param servletContext
	 * @throws ServletException
	 */
	public void init(ServletContext servletContext) throws ServletException {
		this.servletContext = servletContext;
		logger.info("\r\n{}", Copyright.getCopyright());

		try {
			ActionContextFactory actionContextFactory = new ActionContextFactory(this.getClassLoader());
			ActionContext actionContext = actionContextFactory.create(servletContext, this.packages);
			this.setActionContext(actionContext);
		} catch (Exception e) {
			this.destroy();
			throw new ServletException(e);
		}
	}

	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String requestURI = request.getRequestURI();
		String action = this.getAction(request);
		request.setAttribute(SERVLET_CONTEXT, this.getServletContext());
		request.setAttribute("requestURI", requestURI);
		request.setAttribute("contextPath", this.getContextPath(request));

		if (action == null) {
			action = "index";
		}

		boolean b = this.dispatch(request, response, action);

		if (!b) {
			response.sendError(404);
		}
	}

	/**
	 * @param request
	 * @param response
	 * @param action
	 * @return boolean
	 * @throws ServletException
	 * @throws IOException
	 */
	public boolean dispatch(HttpServletRequest request, HttpServletResponse response, String action) throws ServletException, IOException {
		String requestURI = this.getStrictPath(request.getRequestURI());
		request.setAttribute("ActionDispatcher$requestURI", requestURI);
		request.setAttribute("servletContext", this.servletContext);
		Method method = this.actionContext.getMethod(action);
		logger.info("method:{}", method);
		if (method == null) {
			logger.debug("404 - action: {}, requestURI: {}", action, requestURI);
			return false;
		}

		Throwable throwable = null;

		if (logger.isDebugEnabled()) {
			logger.debug("{}: {}.{}(request, response)", action, method.getDeclaringClass().getName(), method.getName());
		}

		try {
			HttpServlet servlet = this.actionContext.getServlet(method);
			method.invoke(servlet, new Object[] { request, response });
		} catch (Throwable t) {
			throwable = t;
			logger.error(t.getMessage(), t);
		}

		if (throwable != null) {
			Throwable t = throwable.getCause();

			if (t != null) {
				throwable = t;
			}

			if (throwable instanceof RuntimeException) {
				throw (RuntimeException) throwable;
			} else if (throwable instanceof ServletException) {
				throw (ServletException) throwable;
			} else {
				throw new ServletException(throwable);
			}
		}
		return true;
	}

	/**
	 * @param packages
	 */
	public void setPackages(String[] packages) {
		this.packages = packages;
	}

	/**
	 * @return String
	 */
	public String[] getPackages() {
		return this.packages;
	}

	/**
	 * @return ServletContext
	 */
	public ServletContext getServletContext() {
		return this.servletContext;
	}

	/**
	 * @param servletContext
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	/**
	 * @return ClassLoader
	 */
	public ClassLoader getClassLoader() {
		return (this.classLoader != null ? this.classLoader : Thread.currentThread().getContextClassLoader());
	}

	/**
	 * @param classLoader
	 */
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	/**
	 * @return the actionContext
	 */
	public ActionContext getActionContext() {
		return this.actionContext;
	}

	/**
	 * @param actionContext the actionContext to set
	 */
	public void setActionContext(ActionContext actionContext) {
		this.actionContext = actionContext;
	}

	/**
	 * @param request
	 * @return String
	 */
	public String getContextPath(HttpServletRequest request) {
		String contextPath = request.getContextPath();

		if (contextPath == null || contextPath.length() <= 1) {
			return "";
		}
		return contextPath;
	}

	/**
	 * @param request
	 * @return String
	 */
	private String getAction(HttpServletRequest request) {
		String queryString = request.getQueryString();

		if (queryString == null) {
			return null;
		}

		int i = queryString.indexOf("action=");

		if (i < 0) {
			return null;
		}

		i += 7;
		int j = queryString.indexOf('&', i);

		if (j < 0) {
			return queryString.substring(i);
		}
		return queryString.substring(i, j);
	}

	/**
	 * @param path
	 * @return String
	 */
	private String getStrictPath(String path) {
		char c;
		StringBuilder buffer = new StringBuilder();

		for (int i = 0, length = path.length(); i < length; i++) {
			c = path.charAt(i);

			if (c == '\\' || c == '/') {
				if (buffer.length() < 1 || buffer.charAt(buffer.length() - 1) != '/') {
					buffer.append("/");
				}
			} else {
				buffer.append(c);
			}
		}
		return buffer.toString();
	}

	/**
	 * destroy
	 */
	public void destroy() {
		this.packages = null;

		if (this.actionContext != null) {
			this.actionContext.destroy();
			this.actionContext = null;
		}
	}
}
