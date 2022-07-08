package com.wfr.springboot.aliyun.service.sls.log.properties;

import com.aliyun.openservices.aliyun.log.producer.ProjectConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Optional;

/**
 * 阿里云SLS日志服务 {@link ProjectConfig} 的属性配置
 *
 * @author wangfarui
 * @since 2022/7/7
 */
@ConfigurationProperties(prefix = "sls.log.project")
public class SlsLogProjectProperties {

    /**
     * 日志服务的项目
     */
    private String project;

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String stsToken;

    private String userAgent;

    /**
     * 日志库
     * <p>所属于 {@link this#project} 下
     */
    private String logStore;

    public static ProjectConfig generateProjectConfig(SlsLogProjectProperties properties) {
        return new ProjectConfig(
                properties.getProject(),
                properties.getEndpoint(),
                properties.getAccessKeyId(),
                properties.getAccessKeySecret(),
                properties.getStsToken(),
                Optional.ofNullable(properties.getUserAgent()).orElse(ProjectConfig.DEFAULT_USER_AGENT)
        );
    }

    public String getProject() {
        return project;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public String getStsToken() {
        return stsToken;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getLogStore() {
        return logStore;
    }
}
