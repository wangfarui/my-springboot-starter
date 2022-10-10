package com.wfr.springboot.base.log.context;

import com.wfr.springboot.base.environment.BaseEnvironment;
import com.wfr.springboot.base.log.context.service.AbstractLogService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 日志记录器
 *
 * @author wangfarui
 * @since 2022/7/12
 */
public abstract class Logger {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Logger.class);

    private static volatile LogService logService;

    /**
     * 获取默认的日志服务
     *
     * @return 默认日志服务
     */
    public static LogService defaultLogService() {
        if (logService == null) {
            synchronized (Logger.class) {
                if (logService == null) {
                    ConfigurableApplicationContext applicationContext = BaseEnvironment.applicationContext();
                    ObjectProvider<LogService> beanProvider = applicationContext.getBeanProvider(LogService.class);
                    beanProvider.ifUnique(l -> logService = l);
                    if (logService == null) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.warn("未找到主日志服务, 确保Primary LogService存在且唯一");
                        }
                        try {
                            logService = applicationContext.getBean(AbstractLogService.DEFAULT_LOG_SERVICE, AbstractLogService.class);
                        } catch (BeansException e) {
                            LOGGER.error("未找到默认日志服务, 确保Default LogService存在", e);
                            throw e;
                        }
                    }
                }
            }
        }
        return logService;
    }

    public static LogData trace() {
        return new LogData(LogLever.TRACE);
    }

    public static void trace(String message) {
        new LogData(LogLever.TRACE).addMessage(message).push();
    }

    public static <T extends LogData> T trace(Class<T> clazz) {
        return generateLogDataInstance(clazz, LogLever.TRACE);
    }

    public static LogData debug() {
        return new LogData(LogLever.DEBUG);
    }

    public static void debug(String message) {
        new LogData(LogLever.DEBUG).addMessage(message).push();
    }

    public static <T extends LogData> T debug(Class<T> clazz) {
        return generateLogDataInstance(clazz, LogLever.DEBUG);
    }

    public static LogData info() {
        return new LogData(LogLever.INFO);
    }

    public static void info(String message) {
        new LogData(LogLever.INFO).addMessage(message).push();
    }

    public static <T extends LogData> T info(Class<T> clazz) {
        return generateLogDataInstance(clazz, LogLever.INFO);
    }

    public static LogData warn() {
        return new LogData(LogLever.WARN);
    }

    public static void warn(String message) {
        new LogData(LogLever.WARN).addMessage(message).push();
    }

    public static <T extends LogData> T warn(Class<T> clazz) {
        return generateLogDataInstance(clazz, LogLever.WARN);
    }

    public static LogData error() {
        return new LogData(LogLever.ERROR);
    }

    public static void error(String message) {
        new LogData(LogLever.ERROR).addMessage(message).push();
    }

    public static <T extends LogData> T error(Class<T> clazz) {
        return generateLogDataInstance(clazz, LogLever.ERROR);
    }

    /**
     * 推送日志消息
     *
     * @param logData 日志信息
     */
    public static void push(LogData logData) {
        LogService logService = defaultLogService();
        push(logData, logService);
    }

    /**
     * 指定日志服务 推送日志消息
     *
     * @param logData    日志信息
     * @param logService 日志服务
     */
    public static void push(LogData logData, LogService logService) {
        logService.push(logData);
    }

    private static <T extends LogData> T generateLogDataInstance(Class<T> clazz, LogLever logLever) {
        try {
            T t = clazz.newInstance();
            t.logLever(logLever);
            return t;
        } catch (Exception e) {
            throw new LogContextException("LogData Class 实例化异常", e);
        }
    }
}
