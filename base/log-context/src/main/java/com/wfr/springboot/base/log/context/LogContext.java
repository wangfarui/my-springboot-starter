package com.wfr.springboot.base.log.context;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * 日志上下文
 *
 * @author wangfarui
 * @since 2022/7/21
 */
public abstract class LogContext {

    /**
     * 日志链路上下文
     */
    private static final ThreadLocal<Map<String, Object>> LOG_TRACE_CONTEXT = new ThreadLocal<>();

    /**
     * 获取当前线程日志链路id
     *
     * @return 日志链路id
     */
    public static String getTraceId() {
        return getLogTraceValue(LogConstants.LOG_TRACE_ID, LogContext::createTraceId);
    }

    /**
     * 获取当前线程日志链路栈
     *
     * @return 日志链路栈
     */
    public static int getTraceStack() {
        Map<String, Object> map = getLogTraceContext();
        AtomicInteger i = (AtomicInteger) map.computeIfAbsent(LogConstants.LOG_TRACE_STACK, t -> new AtomicInteger(1));
        return i.getAndIncrement();
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
     * 获取日志链路指定key的value
     *
     * @param key      日志链路上下文key
     * @param supplier 日志链路上下文value获取方法
     * @param <T>      value数据类型
     * @return 日志链路上下文value
     */
    @SuppressWarnings("unchecked")
    public static <T> T getLogTraceValue(String key, Supplier<T> supplier) {
        Map<String, Object> map = getLogTraceContext();
        Object value;
        if ((value = map.get(key)) == null) {
            synchronized (LOG_TRACE_CONTEXT) {
                if ((value = map.get(key)) == null) {
                    value = supplier.get();
                    map.put(key, value);
                }
            }
        }
        return (T) value;
    }

    /**
     * 获取日志链路上下文
     *
     * @return 日志链路上下文
     */
    public static Map<String, Object> getLogTraceContext() {
        Map<String, Object> map = LOG_TRACE_CONTEXT.get();
        if (map == null) {
            synchronized (LOG_TRACE_CONTEXT) {
                if ((map = LOG_TRACE_CONTEXT.get()) == null) {
                    map = new HashMap<>(4);
                    LOG_TRACE_CONTEXT.set(map);
                }
            }
        }
        return map;
    }

    /**
     * 清除当前线程 traceId
     */
    public static void clearNowTraceId() {
        LOG_TRACE_CONTEXT.remove();
    }
}
