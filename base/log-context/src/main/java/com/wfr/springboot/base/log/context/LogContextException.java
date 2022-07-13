package com.wfr.springboot.base.log.context;

/**
 * 日志上下文异常
 *
 * @author wangfarui
 * @since 2022/7/13
 */
public class LogContextException extends RuntimeException{

    public LogContextException(String message) {
        super(message);
    }

    public LogContextException(String message, Throwable cause) {
        super(message, cause);
    }
}
