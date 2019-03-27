<%@ page pageEncoding="utf-8" isThreadSafe="false" session="false"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.IOException"%>
<%@ page import="java.net.URL"%>
<%@ page import="java.net.URLClassLoader"%>
<%@ page import="java.security.AccessController"%>
<%@ page import="java.security.PrivilegedAction"%>
<%@ page import="java.util.LinkedHashSet"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Locale"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.lang.reflect.Constructor"%>
<%@ page import="java.lang.reflect.Method"%>
<%!
    String loginURL;
    Object dispatcher;
    Class<?> sessionFilterClass;

    /**
     * @version 1.0
     */
    public static class BootstrapClassLoader extends URLClassLoader {
        /**
         * @param urls
         */
        public BootstrapClassLoader(URL[] urls) {
            super(urls);
        }

        /**
         * @param urls
         * @param parent
         */
        public BootstrapClassLoader(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }

        /**
         * @param parent
         * @param lib
         * @return HotBootClassLoader
         */
        public static BootstrapClassLoader getClassLoader(final ClassLoader parent, final File lib) {
            URL[] urls = getRepositories(lib.listFiles());
            return getClassLoader(parent, urls);
        }

        /**
         * @param parent
         * @param repositories
         * @return HotBootClassLoader
         */
        public static BootstrapClassLoader getClassLoader(final ClassLoader parent, final URL[] repositories) {
            ClassLoader classLoader = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
                @Override
                public ClassLoader run() {
                    if(parent == null) {
                        return new BootstrapClassLoader(repositories);
                    }
                    else {
                        return new BootstrapClassLoader(repositories, parent);
                    }
                }
            });
            return (BootstrapClassLoader)classLoader;
        }

        /**
         * @param parent
         * @param repositories
         * @return ClassLoader
         */
        public static ClassLoader getClassLoader(final ClassLoader parent, final List<URL> repositories) {
            URL[] urls = new URL[repositories.size()];
            repositories.toArray(urls);
            return getClassLoader(parent, urls);
        }

        /**
         * @param files
         * @return URL[]
         */
        public static URL[] getRepositories(File[] files) {
            Set<URL> set = new LinkedHashSet<URL>();

            if(files != null) {
                for(File file : files) {
                    String fileName = file.getName().toLowerCase(Locale.ENGLISH);

                    if(fileName.endsWith(".jar") || fileName.endsWith(".zip")) {
                        try {
                            set.add(file.toURI().toURL());
                        }
                        catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            URL[] urls = new URL[set.size()];
            set.toArray(urls);
            return urls;
        }

        /**
         * @param urls
         * @param files
         * @return URL[]
         */
        public static List<URL> addRepositories(List<URL> urls, File[] files) {
            Set<URL> set = new LinkedHashSet<URL>();

            if(files != null) {
                for(File file : files) {
                    String fileName = file.getName().toLowerCase(Locale.ENGLISH);

                    if(fileName.endsWith(".jar") || fileName.endsWith(".zip")) {
                        try {
                            set.add(file.toURI().toURL());
                        }
                        catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            urls.addAll(set);
            return urls;
        }
    }

    /**
     * @param path
     * @return ClassLoader
     * @throws Exception
     */
    protected ClassLoader getClassLoader(String path) throws Exception {
        List<URL> repositories = new ArrayList<URL>();
        repositories.add(new File(path).toURI().toURL());

        ClassLoader parent = this.getClass().getClassLoader();
        return BootstrapClassLoader.getClassLoader(parent, repositories);
    }

    /**
     * @param classLoader
     * @param className
     * @param parameterTypes
     * @param args
     * @return Object
     * @throws Exception
     */
    protected static Object getInstance(ClassLoader classLoader, String className) throws Exception {
        return classLoader.loadClass(className).newInstance();
    }

    /**
     * @param object
     * @param name
     * @param types
     * @param args
     * @return Object
     * @throws Exception
     */
    protected static Object invoke(Object object, String name, Class<?>[] types, Object[] args) throws Exception {
        Method method = object.getClass().getMethod(name, types);
        return method.invoke(object, args);
    }

    /**
     * @param object
     * @param name
     * @param types
     * @param args
     * @throws Exception
     */
    protected static Object invoke(Class<?> type, String name, Class<?>[] types, Object[] args) throws Exception {
        Method method = type.getMethod(name, types);
        return method.invoke(null, args);
    }

    /**
     * @param request
     * @param response
     */
    protected void reload(HttpServletRequest request, HttpServletResponse response) {
        this.uninstall(request, response, false);
        response.setStatus(304);
        response.setHeader("Location", request.getRequestURI());
    }

    /**
     * @param request
     * @param response
     * @param remove
     */
    protected void uninstall(HttpServletRequest request, HttpServletResponse response, boolean remove) {
        if(this.dispatcher != null) {
            try {
                invoke(this.dispatcher, "destroy", null, null);
            }
            catch (Exception e) {
            }
            this.dispatcher = null;
        }

        if(remove) {
            ServletContext servletContext = getServletContext();
            String requestURI = request.getRequestURI();
            String realPath = servletContext.getRealPath(requestURI);
            File jspFile = new File(realPath);

            if(jspFile.exists() && jspFile.getName().endsWith(".jsp")) {
                jspFile.delete();
            }
        }
    }

    /**
     * @param request
     * @return String
     */
    protected static String getContextPath(HttpServletRequest request) {
        String contextPath = request.getContextPath();

        if(contextPath == null || contextPath.equals("/")) {
            return "";
        }
        return contextPath;
    }

    /**
     * @param request
     * @return String
     */
    protected static String getLoginURL(HttpServletRequest request) {
        String loginUrl = request.getRequestURI();
        String contextPath = getContextPath(request);

        if(contextPath.length() > 0) {
            loginUrl = loginUrl.substring(contextPath.length());
        }
        return loginUrl + "?action=finder.login";
    }

    /**
     * @return boolean
     */
    protected static boolean getTrue() {
        return true;
    }
%>
<%
    try {
        response.resetBuffer();
        String action = request.getParameter("action");

        if(action != null) {
            if(action.equals("reload")) {
                this.reload(request, response);
                return;
            }

            if(action.equals("uninstall")) {
                this.uninstall(request, response);
                return;
            }
        }

        if(this.dispatcher == null) {
            ClassLoader classLoader = this.getClassLoader("E:\\WorkSpace\\finder\\finder-web\\webapp\\WEB-INF\\lib\\finder-web-2.2.5.jar");
            Thread.currentThread().setContextClassLoader(classLoader);

            this.loginURL = getLoginURL(request);
            this.sessionFilterClass = classLoader.loadClass("com.skin.finder.filter.SessionFilter");
            this.dispatcher = getInstance(classLoader, "com.skin.finder.web.ActionDispatcher");

            invoke(this.dispatcher, "setPackages", new Class<?>[]{String[].class}, new Object[]{new String[]{
                "com.skin.finder.servlet", "com.skin.finder.admin.servlet"
            }});
            invoke(this.dispatcher, "init", new Class<?>[]{ServletContext.class}, new Object[]{application});
        }

        Object flag = invoke(this.sessionFilterClass, "check", new Class<?>[]{HttpServletRequest.class, HttpServletResponse.class, String.class}, new Object[]{request, response, this.loginURL});

        if(flag.equals(Boolean.TRUE)) {
            invoke(this.dispatcher, "service",
                    new Class<?>[]{HttpServletRequest.class, HttpServletResponse.class},
                    new Object[]{request, response});
        }
    }
    catch(Throwable t) {
        throw new ServletException(t);
    }
    finally {
        response.flushBuffer();
        out.clear();
        out = pageContext.pushBody();
    }

    if(getTrue()) {
        return;
    }
%>
