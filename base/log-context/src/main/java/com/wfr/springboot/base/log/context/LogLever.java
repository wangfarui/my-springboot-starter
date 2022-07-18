package com.wfr.springboot.base.log.context;

/**
 * 日志级别
 *
 * @author wangfarui
 * @since 2022/7/12
 */
public enum LogLever {
    TRACE,
    DEBUG,
    INFO,
    WARN,
    ERROR;

    public static LogLever LOWEST_LOG_LEVER = LogLever.TRACE;

    public static LogLever HIGHEST_LOG_LEVER = LogLever.ERROR;

}
