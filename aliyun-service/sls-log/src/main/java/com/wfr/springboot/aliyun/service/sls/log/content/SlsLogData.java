package com.wfr.springboot.aliyun.service.sls.log.content;

import com.wfr.springboot.base.environment.BaseEnvironment;
import com.wfr.springboot.base.log.context.LogData;
import com.wfr.springboot.base.log.context.LogLever;

/**
 * 阿里云SLS日志信息数据
 *
 * @author wangfarui
 * @since 2022/7/7
 */
public class SlsLogData extends LogData {

    /**
     * 日志主题
     */
    private String topic;

    /**
     * 日志来源
     */
    private String source;

    public SlsLogData() {
        this(LogLever.INFO, "");
    }

    public SlsLogData(LogLever logLever, String topic) {
        this(logLever, topic, BaseEnvironment.serverIp);
    }

    public SlsLogData(LogLever logLever, String topic, String source) {
        super(logLever);
        this.topic = topic;
        this.source = source;
    }

    public static SlsLogData trace(String topic) {
        return createLogData(LogLever.TRACE, topic);
    }

    public static SlsLogData debug(String topic) {
        return createLogData(LogLever.DEBUG, topic);
    }

    public static SlsLogData info(String topic) {
        return createLogData(LogLever.INFO, topic);
    }

    public static SlsLogData warn(String topic) {
        return createLogData(LogLever.WARN, topic);
    }

    public static SlsLogData error(String topic) {
        return createLogData(LogLever.ERROR, topic);
    }

    public static SlsLogData createLogData(LogLever logLever, String topic) {
        return new SlsLogData(logLever, topic);
    }

    public String getTopic() {
        return topic;
    }

    public SlsLogData setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public String getSource() {
        return source;
    }

    public SlsLogData setSource(String source) {
        this.source = source;
        return this;
    }
}
