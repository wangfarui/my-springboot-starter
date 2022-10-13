package com.wfr.springboot.base.dao.context.properties;

import com.wfr.springboot.base.log.context.LogLever;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * 数据访问层 SQL 日志属性配置
 *
 * @author wangfarui
 * @since 2022/7/27
 */
@ConfigurationProperties(prefix = "dao.sql.log")
public class DaoSqlLogProperties {

    /**
     * 正常sql日志级别
     */
    private LogLever normalSqlLogLevel = LogLever.INFO;

    /**
     * 慢sql日志级别
     */
    private LogLever slowSqlLogLevel = LogLever.WARN;

    /**
     * 异常sql日志级别
     */
    private LogLever errorSqlLogLevel = LogLever.ERROR;

    /**
     * 慢sql时间
     */
    @DurationUnit(ChronoUnit.MILLIS)
    private Duration slowSqlTime = Duration.ofMillis(1000L);

    /**
     * 简化sql语句
     * <p>默认true, 将所有sql语句格式化为一行; false, 原样输出sql语句</p>
     */
    private boolean simplifySql = true;

    public LogLever getNormalSqlLogLevel() {
        return normalSqlLogLevel;
    }

    public void setNormalSqlLogLevel(LogLever normalSqlLogLevel) {
        this.normalSqlLogLevel = normalSqlLogLevel;
    }

    public LogLever getSlowSqlLogLevel() {
        return slowSqlLogLevel;
    }

    public void setSlowSqlLogLevel(LogLever slowSqlLogLevel) {
        this.slowSqlLogLevel = slowSqlLogLevel;
    }

    public LogLever getErrorSqlLogLevel() {
        return errorSqlLogLevel;
    }

    public void setErrorSqlLogLevel(LogLever errorSqlLogLevel) {
        this.errorSqlLogLevel = errorSqlLogLevel;
    }

    public Duration getSlowSqlTime() {
        return slowSqlTime;
    }

    public void setSlowSqlTime(Duration slowSqlTime) {
        this.slowSqlTime = slowSqlTime;
    }

    public boolean isSimplifySql() {
        return simplifySql;
    }

    public void setSimplifySql(boolean simplifySql) {
        this.simplifySql = simplifySql;
    }
}
