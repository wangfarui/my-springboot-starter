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
     * 日志开始时间 (时间戳)
     */
    private final long startTime;

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

    public static LogData createLogData(LogLever logLever) {
        Assert.notNull(logLever, "log lever must be not null");
        return new LogData(logLever);
    }

    public LogData logLever(LogLever logLever) {
        this.logLever = logLever;
        return this;
    }

    public LogLever getLogLever() {
        return this.logLever;
    }

    public LogData addLogContents(Map<String, Object> logContents) {
        if (!CollectionUtils.isEmpty(logContents)) {
            this.logContents.putAll(logContents);
        }
        return this;
    }

    public Map<String, Object> getLogContents() {
        return logContents;
    }

    public Long getStartTime() {
        return this.startTime;
    }

    public LogData addMessage(Object message) {
        return add(LogConstants.LOG_MESSAGE, message);
    }

    public LogData addException(Throwable throwable) {
        // TODO 异常信息需要打印堆栈链
        return add(LogConstants.LOG_EXCEPTION, throwable);
    }

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

    public void put() {
        Logger.put(this);
    }
}
