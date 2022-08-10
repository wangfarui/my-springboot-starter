package com.wfr.springboot.base.environment;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

/**
 * 基础服务的环境信息
 *
 * @author wangfarui
 * @since 2022/7/7
 */
public abstract class BaseEnvironment {

    /**
     * 服务器IP
     */
    private static String serverIp = "localhost";

    /**
     * 服务名称
     * <p>对应${spring.application.name}
     */
    private static String serverName;

    /**
     * spring 应用上下文
     */
    private static ConfigurableApplicationContext applicationContext;

    /**
     * java util 日志器
     */
    private final static Logger LOGGER = Logger.getLogger("BaseEnvironment");

    /**
     * 配置应用上下文
     *
     * @param applicationContext {@link ConfigurableApplicationContext}
     */
    public static void setApplicationContext(ConfigurableApplicationContext applicationContext) {
        BaseEnvironment.applicationContext = applicationContext;
    }

    /**
     * 环境配置
     */
    public static void configEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            BaseEnvironment.serverIp = localHost.getHostAddress();
        } catch (UnknownHostException e) {
            LOGGER.warning("无法获取本地Host");
        }

        String applicationName = environment.getProperty("spring.application.name");
        if (applicationName != null) {
            BaseEnvironment.serverName = applicationName;
        }
    }

    /**
     * 获取当前环境 Spring 应用上下文
     */
    public static ConfigurableApplicationContext applicationContext() {
        return applicationContext;
    }

    /**
     * 获取当前环境的服务器ip地址
     *
     * @return Server IP
     */
    public static String serverIp() {
        return serverIp;
    }

    /**
     * 获取当前环境的服务名称
     *
     * @return Server Name
     */
    public static String serverName() {
        return serverName;
    }
}
