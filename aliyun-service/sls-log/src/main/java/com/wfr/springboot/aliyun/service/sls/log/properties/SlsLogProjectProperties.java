package com.wfr.springboot.aliyun.service.sls.log.properties;

import com.aliyun.openservices.aliyun.log.producer.ProjectConfig;
import com.wfr.springboot.base.environment.BaseEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Optional;

/**
 * 阿里云SLS日志服务 {@link ProjectConfig} 的属性配置
 *
 * @author wangfarui
 * @since 2022/7/7
 */
@ConfigurationProperties(prefix = "aliyun.sls.log")
public class SlsLogProjectProperties implements InitializingBean {

    /**
     * 日志服务的项目
     */
    private String project;

    /**
     * 日志库
     * <p>所属于 {@link this#project} 下
     */
    private String logStore;

    /**
     * 连接点（地名）
     */
    private String endpoint;

    /**
     * 用户标识ID
     */
    private String accessKeyId;

    /**
     * 用户验证密钥
     */
    private String accessKeySecret;

    private String stsToken;

    private String userAgent;

    public static ProjectConfig generateProjectConfig(SlsLogProjectProperties properties) {
        try {
            return new ProjectConfig(
                    properties.getProject(),
                    properties.getEndpoint(),
                    properties.getAccessKeyId(),
                    properties.getAccessKeySecret(),
                    properties.getStsToken(),
                    Optional.ofNullable(properties.getUserAgent()).orElse(ProjectConfig.DEFAULT_USER_AGENT)
            );
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("阿里云SLS日志服务参数配置异常", e);
        }
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getStsToken() {
        return stsToken;
    }

    public void setStsToken(String stsToken) {
        this.stsToken = stsToken;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getLogStore() {
        return logStore;
    }

    public void setLogStore(String logStore) {
        this.logStore = logStore;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.logStore == null) {
            this.logStore = BaseEnvironment.serverName;
        }
    }
}
