package com.wfr.springboot.base.web.context.service;

import com.wfr.springboot.base.log.context.LogData;
import com.wfr.springboot.base.log.context.LogService;
import com.wfr.springboot.base.log.context.service.AbstractLogService;

/**
 * web请求的日志服务
 *
 * @author wangfarui
 * @since 2022/7/15
 */
public class WebRequestLogService implements LogService {

    private final AbstractLogService delegate;

    public WebRequestLogService(AbstractLogService logService) {
        this.delegate = logService;
    }

    @Override
    public void push(LogData logData) {
        this.delegate.push(logData, true);
    }

}
