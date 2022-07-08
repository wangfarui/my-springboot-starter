package com.wfr.springboot.aliyun.service.sls.log.service;

import com.wfr.springboot.aliyun.service.sls.log.content.SlsLogData;

/**
 * 阿里云SLS日志生产者服务
 *
 * @author wangfarui
 * @since 2022/7/7
 */
public interface SlsLogProducerService {

    String SERVICE_BEAN_NAME = "slsLogProducerService";

    void send(SlsLogData logData);
}
