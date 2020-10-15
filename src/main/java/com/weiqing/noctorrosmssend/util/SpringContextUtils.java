package com.weiqing.noctorrosmssend.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Locale;

public final class SpringContextUtils {
    private static Environment environment;
    private static ApplicationContext context;

    public static void cleanContext() {
        context = null;
    }

    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    /**
     * 根据类型获取spring容器里的bean实例
     *
     * @param clazz 类型
     * @return bean实例
     */
    public static <T> T getBean(Class<T> clazz) {
        return context.getAutowireCapableBeanFactory().getBean(clazz);
    }

    public static String getMessage(String key) {
        return context.getMessage(key, null, Locale.getDefault());
    }

    public static String getProperty(String key) {
        return environment.getProperty(key);
    }

    public static void setEnvironment(Environment environment) {
        SpringContextUtils.environment = environment;
    }

    public static void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringContextUtils.context = context;
    }

}