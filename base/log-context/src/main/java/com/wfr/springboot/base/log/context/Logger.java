package com.wfr.springboot.base.log.context;

import com.wfr.springboot.base.environment.BaseEnvironment;
import org.springframework.beans.factory.ObjectProvider;

/**
 * 日志记录器
 *
 * @author wangfarui
 * @since 2022/7/12
 */
public abstract class Logger {

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
                    ObjectProvider<LogService> beanProvider = BaseEnvironment
                            .applicationContext()
                            .getBeanProvider(LogService.class);
                    beanProvider.ifUnique(l -> logService = l);
                    if (logService == null) {
                        throw new IllegalStateException("未找到默认日志服务, 确保LogService存在且唯一");
                    }
                }
            }
        }
        return logService;
    }

    public static <T extends LogData> T trace(Class<T> clazz) {
        return generateLogDataInstance(clazz, LogLever.TRACE);
    }

    public static <T extends LogData> T debug(Class<T> clazz) {
        return generateLogDataInstance(clazz, LogLever.DEBUG);
    }

    public static <T extends LogData> T info(Class<T> clazz) {
        return generateLogDataInstance(clazz, LogLever.INFO);
    }

    public static <T extends LogData> T warn(Class<T> clazz) {
        return generateLogDataInstance(clazz, LogLever.WARN);
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
