package com.wfr.springboot.aliyun.service.sls.log.service;

import com.aliyun.openservices.aliyun.log.producer.LogProducer;
import com.aliyun.openservices.aliyun.log.producer.Producer;
import com.aliyun.openservices.aliyun.log.producer.ProducerConfig;
import com.aliyun.openservices.aliyun.log.producer.ProjectConfig;
import com.aliyun.openservices.log.common.LogItem;
import com.wfr.springboot.aliyun.service.sls.log.content.SlsLogData;
import com.wfr.springboot.aliyun.service.sls.log.properties.SlsLogProducerProperties;
import com.wfr.springboot.aliyun.service.sls.log.properties.SlsLogProjectProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;

/**
 * 阿里云SLS日志生产者服务 默认实现类
 *
 * @author wangfarui
 * @since 2022/7/7
 */
public class DefaultSlsLogProducerService implements SlsLogProducerService, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSlsLogProducerService.class);

    private final SlsLogProjectProperties projectProperties;

    private final SlsLogProducerProperties producerProperties;

    private final String project;

    private final String logStore;

    private Producer producer;

    public DefaultSlsLogProducerService(SlsLogProjectProperties projectProperties,
                                        SlsLogProducerProperties producerProperties) {
        this.projectProperties = projectProperties;
        this.producerProperties = producerProperties;

        this.project = projectProperties.getProject();
        this.logStore = projectProperties.getLogStore();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (producer == null) {
            ProducerConfig producerConfig = new ProducerConfig();
            fillProducerConfig(producerConfig);

            LogProducer logProducer = new LogProducer(producerConfig);
            fillLogProducer(logProducer);

            this.producer = logProducer;
        }
    }

    @Override
    public void send(SlsLogData logData) {
        LogItem logItem = logData.packaging();
        List<LogItem> logItems = Collections.singletonList(logItem);
        try {
            producer.send(this.project, this.logStore, logItems);
        } catch (Exception e) {
            LOGGER.error("Sls log sending exception", e);
        }
    }

    private void fillProducerConfig(ProducerConfig producerConfig) {
        SlsLogProducerProperties properties = this.producerProperties;
        if (properties.getLingerMs() >= ProducerConfig.LINGER_MS_LOWER_LIMIT) {
            producerConfig.setLingerMs(properties.getLingerMs());
        }
        producerConfig.setRetries(properties.getRetries());
    }

    private void fillLogProducer(LogProducer logProducer) {
        ProjectConfig projectConfig = SlsLogProjectProperties.generateProjectConfig(this.projectProperties);
        logProducer.putProjectConfig(projectConfig);
    }
}
