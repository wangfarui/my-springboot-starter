package com.wfr.springboot.aliyun.service.sls.log.content;

/**
 * 阿里云SLS日志级别
 *
 * @author wangfarui
 * @since 2022/7/7
 */
public enum SlsLogLever {
    ERROR(4, "ERROR"),
    WARN(3, "WARN"),
    INFO(2, "INFO"),
    DEBUG(1, "DEBUG"),
    TRACE(0, "TRACE");

    private final int level;

    private final String levelStr;

    SlsLogLever(int level, String levelStr) {
        this.level = level;
        this.levelStr = levelStr;
    }

    public int getLevel() {
        return this.level;
    }

    @Override
    public String toString() {
        return this.levelStr;
    }
}
