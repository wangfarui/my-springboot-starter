package com.wfr.springboot.base.log.context.interceptor;

import com.wfr.springboot.base.log.context.LogConstants;
import com.wfr.springboot.base.log.context.LogData;
import com.wfr.springboot.base.log.context.LogService;

/**
 * 基本信息填充者 日志拦截器
 *
 * @author wangfarui
 * @since 2022/7/12
 */
public class BasicInformationFillerLogInterceptor implements LogInterceptor {

    @Override
    public void processBeforePutLog(LogService logService, LogData logData) {
        long useTime = System.currentTimeMillis() - logData.getStartTime();
        logData.add(LogConstants.LOG_LEVEL, logData.getLogLever());
        logData.add(LogConstants.LOG_USE_TIME, useTime + "ms");
    }
}
