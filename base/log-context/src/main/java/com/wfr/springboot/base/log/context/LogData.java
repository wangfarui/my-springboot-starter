package com.wfr.springboot.base.log.context;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志数据存储容器
 *
 * @author wangfarui
 * @since 2022/7/12
 */
public class LogData {

    /**
     * 日志级别
     */
    private LogLever logLever;

    /**
     * 默认16个key的容量
     */
    private Map<String, Object> logContents = new HashMap<>();

    /**
     * 日志开始时间 (毫秒时间戳)
     */
    private final long startTime;

    /**
     * 空日志实体
     */
    private static final LogData EMPTY_LOG_DATA = new LogData();

    public LogData() {
        this(LogLever.INFO);
    }

    public LogData(LogLever logLever) {
        this.logLever = logLever;
        this.startTime = System.currentTimeMillis();
    }

    public static LogData trace() {
        return createLogData(LogLever.TRACE);
    }

    public static LogData debug() {
        return createLogData(LogLever.DEBUG);
    }

    public static LogData info() {
        return createLogData(LogLever.INFO);
    }

    public static LogData warn() {
        return createLogData(LogLever.WARN);
    }

    public static LogData error() {
        return createLogData(LogLever.ERROR);
    }

    /**
     * 创建日志实体
     *
     * @param logLever 日志级别
     * @return LogData
     */
    public static LogData createLogData(LogLever logLever) {
        Assert.notNull(logLever, "log lever must be not null");
        return new LogData(logLever);
    }

    /**
     * 生成空日志实体, 一般用于try方法外做初始化
     *
     * @return LogData
     */
    public static LogData emptyLogData() {
        return EMPTY_LOG_DATA;
    }

    /**
     * 是否为空日志实体
     *
     * @return true -> 是
     */
    public boolean isEmptyLogData() {
        return this == EMPTY_LOG_DATA;
    }

    /**
     * 配置日志级别
     *
     * @param logLever 日志级别
     * @return LogData
     */
    public LogData logLever(LogLever logLever) {
        this.logLever = logLever;
        return this;
    }

    /**
     * 获取日志级别
     *
     * @return LogLever
     */
    public LogLever getLogLever() {
        return this.logLever;
    }

    /**
     * 批量添加日志内容
     *
     * @param logContents {@link Map<String, Object>}
     * @return LogData
     */
    public LogData addLogContents(Map<String, Object> logContents) {
        if (!CollectionUtils.isEmpty(logContents)) {
            this.logContents.putAll(logContents);
        }
        return this;
    }

    /**
     * 获取全部日志内容
     *
     * @return LogContents
     */
    public Map<String, Object> getLogContents() {
        return logContents;
    }

    /**
     * 获取日志开始时间
     *
     * @return 时间戳(毫秒)
     */
    public Long getStartTime() {
        return this.startTime;
    }

    /**
     * 添加message信息
     *
     * @param message 消息信息
     * @return LogData
     */
    public LogData addMessage(Object message) {
        return add(LogConstants.LOG_MESSAGE, message);
    }

    /**
     * 添加异常信息
     *
     * @param throwable 异常信息调用栈
     * @return LogData
     */
    public LogData addException(Throwable throwable) {
        // TODO 异常信息需要打印堆栈链
        return add(LogConstants.LOG_EXCEPTION, throwable);
    }

    /**
     * 添加请求链路id
     *
     * @param traceId 链路id
     * @return LogData
     */
    public LogData addTraceId(String traceId) {
        return add(LogConstants.LOG_TRACE_ID, traceId);
    }

    /**
     * 添加日志内容
     *
     * @param key     日志key
     * @param content 日志value
     * @return LogData
     */
    public LogData add(String key, Object content) {
        logContents.put(key, content);
        return this;
    }

    /**
     * 格式化日志信息
     *
     * @return json格式的字符串
     */
    public String formatLogContents() {
        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<String, Object> entry : this.logContents.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
        }
        sb.deleteCharAt(sb.length() - 1).append("}");
        return sb.toString();
    }

    /**
     * 推送日志
     */
    public void push() {
        Logger.push(this);
    }
}
