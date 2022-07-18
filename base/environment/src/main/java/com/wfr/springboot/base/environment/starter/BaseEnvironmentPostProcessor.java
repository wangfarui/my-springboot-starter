package com.wfr.springboot.base.environment.starter;

import com.wfr.springboot.base.environment.BaseEnvironment;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 基础环境配置源
 *
 * @author wangfarui
 * @since 2022/7/12
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class BaseEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    public static final int BASE_ENVIRONMENT_POST_PROCESSOR_ORDER = Ordered.HIGHEST_PRECEDENCE;

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        BaseEnvironment.configEnvironment(environment, application);
    }

    @Override
    public int getOrder() {
        return BASE_ENVIRONMENT_POST_PROCESSOR_ORDER;
    }
}
