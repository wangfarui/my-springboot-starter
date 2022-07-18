package com.wfr.springboot.base.environment.starter;

import com.wfr.springboot.base.environment.BaseEnvironment;
import lombok.NonNull;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;

/**
 * 基础环境服务初始化器
 *
 * @author wangfarui
 * @since 2022/7/7
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class BaseEnvironmentInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

    public static final int BASE_ENVIRONMENT_INITIALIZER_ORDER = Ordered.HIGHEST_PRECEDENCE;

    @Override
    public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
        BaseEnvironment.setApplicationContext(applicationContext);
    }

    @Override
    public int getOrder() {
        return BASE_ENVIRONMENT_INITIALIZER_ORDER;
    }
}
