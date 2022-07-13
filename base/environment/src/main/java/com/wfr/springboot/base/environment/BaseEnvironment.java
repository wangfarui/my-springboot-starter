package com.wfr.springboot.base.environment;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 基础服务的环境信息
 *
 * @author wangfarui
 * @since 2022/7/7
 */
public abstract class BaseEnvironment {

    private static ConfigurableApplicationContext applicationContext;

    /**
     * 服务器IP
     */
    public static String serverIp;

    /**
     * 服务名称
     * <p>对应${spring.application.name}
     */
    public static String serverName;

    public static void setApplicationContext(ConfigurableApplicationContext applicationContext) {
        BaseEnvironment.applicationContext = applicationContext;
    }

    /**
     * 获取当前环境 Spring 应用上下文
     */
    public static ConfigurableApplicationContext applicationContext() {
        return applicationContext;
    }

    /**
     * 环境配置
     */
    public static void configEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            BaseEnvironment.serverIp = localHost.getHostAddress();

            String applicationName = environment.getProperty("spring.application.name");
            if (applicationName != null) {
                BaseEnvironment.serverName = applicationName;
            }
        } catch (UnknownHostException e) {
            BaseEnvironment.serverIp = "localhost";
            System.err.println("无法获取本地Host");
        }
    }
}
