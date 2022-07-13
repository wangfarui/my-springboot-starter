package com.wfr.springboot.base.log.context.properties;

import com.wfr.springboot.base.log.context.LogLever;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 日志上下文属性配置
 *
 * @author wangfarui
 * @since 2022/7/12
 */
@ConfigurationProperties(prefix = "log")
public class LogContextProperties {

    private LogLever lever = LogLever.INFO;

    public LogLever getLever() {
        return lever;
    }

    public void setLever(LogLever lever) {
        this.lever = lever;
    }
}
