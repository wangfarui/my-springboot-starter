package com.wfr.springboot.base.log.context.interceptor;

import com.wfr.springboot.base.log.context.LogData;
import com.wfr.springboot.base.log.context.LogService;

/**
 * 日志拦截器
 *
 * @author wangfarui
 * @since 2022/7/12
 */
public interface LogInterceptor {

    default void processBeforePutLog(LogService logService, LogData logData) {

    }

    default void processAfterPutLog(LogService logService, LogData logData) {

    }
}
