package com.wfr.springboot.aliyun.service.sls.log.properties;

import com.aliyun.openservices.aliyun.log.producer.ProjectConfig;
import com.wfr.springboot.base.environment.BaseEnvironment;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

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
     * 开启阿里云SLS日志服务
     */
    private Boolean enabled = true;

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
        return new ProjectConfig(
                properties.getProject(),
                properties.getEndpoint(),
                properties.getAccessKeyId(),
                properties.getAccessKeySecret(),
                properties.getStsToken(),
                Optional.ofNullable(properties.getUserAgent()).orElse(ProjectConfig.DEFAULT_USER_AGENT)
        );
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.logStore == null) {
            this.logStore = BaseEnvironment.serverName();
        }
        checkProjectPropertiesValue();
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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

    private void checkProjectPropertiesValue() {
        if (StringUtils.isEmpty(this.project)) {
            throwNullValueException("project");
        }
        if (StringUtils.isEmpty(this.logStore)) {
            throwNullValueException("logStore");
        }
        if (StringUtils.isEmpty(this.endpoint)) {
            throwNullValueException("endpoint");
        }
        if (StringUtils.isEmpty(this.accessKeyId)) {
            throwNullValueException("accessKeyId");
        }
        if (StringUtils.isEmpty(this.accessKeySecret)) {
            throwNullValueException("accessKeySecret");
        }
    }

    private void throwNullValueException(String key) {
        throw new IllegalArgumentException("阿里云SLS日志参数[" + key + "]配置不能为空, 请检查配置文件");
    }
}
