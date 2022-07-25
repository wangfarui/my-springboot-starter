package com.wfr.springboot.base.log.context;

import java.util.UUID;

/**
 * 日志上下文
 *
 * @author wangfarui
 * @since 2022/7/21
 */
public abstract class LogContext {

    /**
     * 日志链路id上下文
     */
    private static final ThreadLocal<String> LOG_TRACE_ID_CONTEXT = new ThreadLocal<>();

    /**
     * 获取当前线程日志链路id
     *
     * @return 日志链路id
     */
    public static String getTraceId() {
        String traceId = LOG_TRACE_ID_CONTEXT.get();
        if (traceId == null) {
            traceId = createTraceId();
            LOG_TRACE_ID_CONTEXT.set(traceId);
        }
        return traceId;
    }

    /**
     * 创建日志链路id
     *
     * @return 日志链路id
     */
    public static String createTraceId() {
        return UUID.randomUUID().toString();
    }

    /**
     * 清除当前线程 traceId
     */
    public static void clearNowTraceId() {
        LOG_TRACE_ID_CONTEXT.remove();
    }
}
