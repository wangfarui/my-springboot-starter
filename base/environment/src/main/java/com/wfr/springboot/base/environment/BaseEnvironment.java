package com.wfr.springboot.base.environment;

import org.springframework.context.ConfigurableApplicationContext;

/**
 * 基础服务的环境信息
 *
 * @author wangfarui
 * @since 2022/7/7
 */
public abstract class BaseEnvironment {

    private static ConfigurableApplicationContext applicationContext;

    public static void setApplicationContext(ConfigurableApplicationContext applicationContext) {
        BaseEnvironment.applicationContext = applicationContext;
    }

    public static ConfigurableApplicationContext applicationContext() {
        return applicationContext;
    }
}
