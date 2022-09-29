package com.wfr.springboot.base.log.context;

/**
 * 日志常量
 *
 * @author wangfarui
 * @since 2022/7/12
 */
public abstract class LogConstants {

    /**
     * 日志链路ID key
     */
    public static final String LOG_TRACE_ID = "log_trace_id";

    /**
     * 日志链路栈 key
     * <p>表示日志的执行顺序</p>
     */
    public static final String LOG_TRACE_STACK = "log_trace_stack";

    /**
     * 日志服务级别 key
     */
    public static final String LOG_LEVEL = "log_level";

    /**
     * 日志服务异常信息 key
     */
    public static final String LOG_EXCEPTION = "log_exception";

    /**
     * 日志服务使用时长 key
     */
    public static final String LOG_USE_TIME = "log_use_time";

    /**
     * 日志服务消息 key
     */
    public static final String LOG_MESSAGE = "log_message";

}
