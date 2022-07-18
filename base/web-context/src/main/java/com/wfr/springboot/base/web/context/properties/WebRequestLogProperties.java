package com.wfr.springboot.base.web.context.properties;

import com.wfr.springboot.base.log.context.LogLever;
import com.wfr.springboot.base.web.context.starter.WebRequestLogAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * web请求链路日志属性
 *
 * @author wangfarui
 * @since 2022/7/18
 */
@ConfigurationProperties(prefix = WebRequestLogAutoConfiguration.WEB_REQUEST_LOG_PROPERTIES_PREFIX)
public class WebRequestLogProperties {

    /**
     * web请求链路日志开关
     */
    private boolean enabled = true;

    /**
     * web请求链路日志的日志级别
     */
    private LogLever lever = LogLever.TRACE;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LogLever getLever() {
        return lever;
    }

    public void setLever(LogLever lever) {
        this.lever = lever;
    }
}
