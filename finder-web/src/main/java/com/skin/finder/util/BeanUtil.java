/*
 * $RCSfile: BeanUtil.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: BeanUtil</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class BeanUtil {
    private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);
    private static final Object[] EMPTY_PARAMETER = new Object[0];
    private static final Class<?>[] EMPTY_PARAMETER_TYPE = new Class[0];

    /**
     * @param <T>
     * @param bean
     * @param properties
     * @return <T>
     */
    public static <T> T setProperties(T bean, Properties properties) {
        if(bean == null || properties == null) {
            return bean;
        }

        String name = null;
        Method[] methods = bean.getClass().getMethods();

        for(int i = 0; i < methods.length; i++) {
            name = methods[i].getName();

            if(name.startsWith("set") && name.length() > 3) {
                try {
                    name = Character.toLowerCase(name.charAt(3)) + name.substring(4);
                    Class<?>[] parameterTypes = methods[i].getParameterTypes();

                    if(parameterTypes != null && parameterTypes.length == 1) {
                        Object value = ClassUtil.cast(properties.getProperty(name), parameterTypes[0]);

                        if(value != null) {
                            methods[i].invoke(bean, new Object[]{value});
                        }
                    }
                    else {
                    }
                }
                catch(SecurityException e) {
                    if(logger.isWarnEnabled()) {
                        logger.warn(e.getMessage(), e);
                    }
                }
                catch(IllegalArgumentException e) {
                    if(logger.isWarnEnabled()) {
                        logger.warn(e.getMessage(), e);
                    }
                }
                catch(IllegalAccessException e) {
                    if(logger.isWarnEnabled()) {
                        logger.warn(e.getMessage(), e);
                    }
                }
                catch(InvocationTargetException e) {
                    if(logger.isWarnEnabled()) {
                        logger.warn(e.getMessage(), e);
                    }
                }
            }
        }

        return bean;
    }

    /**
     * @param <T>
     * @param source
     * @param target
     * @param recursive
     * @return T
     */
    public static <T> T copy(Object source, T target, boolean recursive) {
        if(source != null && target != null) {
            Class<?> clazz1 = source.getClass();
            Class<?> clazz2 = target.getClass();
            Method[] methods = clazz1.getMethods();

            for(int i = 0; i < methods.length; i++) {
                String name = methods[i].getName();
                Class<?>[] parameterTypes = methods[i].getParameterTypes();

                if(name.startsWith("set") && parameterTypes.length == 1) {
                    try {
                        Method getMethod = clazz1.getMethod("get" + name.substring(3), EMPTY_PARAMETER_TYPE);
                        Object value = getMethod.invoke(source, EMPTY_PARAMETER);

                        if(value != null) {
                            Method setMethod = clazz2.getMethod("set" + name.substring(3), parameterTypes);

                            if(setMethod != null) {
                                if(recursive && (value instanceof java.lang.Cloneable)) {
                                    Object clone = BeanUtil.invoke(value, "clone");

                                    if(clone != null) {
                                        setMethod.invoke(target, new Object[]{clone});
                                    }
                                }
                                else {
                                    setMethod.invoke(target, new Object[]{value});
                                }
                            }
                        }
                    }
                    catch(SecurityException e) {
                        if(logger.isDebugEnabled()) {
                            logger.debug("SecurityException: " + e.getMessage());
                        }
                    }
                    catch(NoSuchMethodException e) {
                        if(logger.isDebugEnabled()) {
                            logger.debug("NoSuchMethodException: " + e.getMessage());
                        }
                    }
                    catch(IllegalArgumentException e) {
                        if(logger.isDebugEnabled()) {
                            logger.debug("IllegalArgumentException: " + e.getMessage());
                        }
                    }
                    catch(IllegalAccessException e) {
                        if(logger.isDebugEnabled()) {
                            logger.debug("IllegalAccessException: " + e.getMessage());
                        }
                    }
                    catch(InvocationTargetException e) {
                        if(logger.isDebugEnabled()) {
                            logger.debug("InvocationTargetException: " + e.getMessage());
                        }
                    }
                }
            }
        }

        return target;
    }

    /**
     * @param object
     * @param methodName
     * @return Object
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static Object invoke(Object object, String methodName) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return object.getClass().getMethod(methodName, EMPTY_PARAMETER_TYPE).invoke(object, EMPTY_PARAMETER);
    }
}
