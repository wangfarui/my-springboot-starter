package com.wfr.springboot.base.log.context.service;

import com.wfr.springboot.base.log.context.LogData;
import com.wfr.springboot.base.log.context.LogLever;
import com.wfr.springboot.base.log.context.LogService;
import com.wfr.springboot.base.log.context.interceptor.LogInterceptor;
import com.wfr.springboot.base.log.context.properties.LogContextProperties;

import java.util.List;

/**
 * 日志服务抽象层
 *
 * @author wangfarui
 * @since 2022/7/12
 */
public abstract class AbstractLogService implements LogService {

    private LogContextProperties logContextProperties;

    private List<LogInterceptor> logInterceptors;

    public AbstractLogService(LogContextProperties logContextProperties, List<LogInterceptor> logInterceptors) {
        this.logContextProperties = logContextProperties;
        this.logInterceptors = logInterceptors;
    }

    @Override
    public LogLever logLever() {
        return logContextProperties.getLever();
    }

    @Override
    public void put(LogData logData) {
        if (!canPut(logData.getLogLever())) {
            return;
        }
        logInterceptors.forEach(logInterceptor -> logInterceptor.processBeforePutLog(this, logData));
        doPut(logData);
        logInterceptors.forEach(logInterceptor -> logInterceptor.processAfterPutLog(this, logData));
    }

    protected void doPut(LogData logData) {

    }
}
