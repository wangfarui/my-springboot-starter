package com.wfr.springboot.base.log.context;

/**
 * 日志服务
 *
 * @author wangfarui
 * @since 2022/7/12
 */
public interface LogService {

    /**
     * 日志级别
     *
     * @return 日志级别
     */
    default LogLever logLever() {
        return LogLever.LOWEST_LOG_LEVER;
    }

    /**
     * 是否可以推送日志
     *
     * @return true -> 可以
     */
    default boolean canPut(LogLever logLever) {
        return logLever().ordinal() <= logLever.ordinal();
    }

    /**
     * 推送日志
     *
     * @param logData 日志信息
     */
    void put(LogData logData);

}
