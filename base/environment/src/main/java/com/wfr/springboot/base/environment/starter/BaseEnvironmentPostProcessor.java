package com.wfr.springboot.base.environment.starter;

import com.wfr.springboot.base.environment.BaseEnvironment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 基础环境配置源
 *
 * @author wangfarui
 * @since 2022/7/12
 */
public class BaseEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        BaseEnvironment.configEnvironment(environment, application);
    }
}
