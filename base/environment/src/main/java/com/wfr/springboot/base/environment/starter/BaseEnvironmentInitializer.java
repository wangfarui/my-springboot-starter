package com.wfr.springboot.base.environment.starter;

import com.wfr.springboot.base.environment.BaseEnvironment;
import lombok.NonNull;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 基础环境服务初始化器
 *
 * @author wangfarui
 * @since 2022/7/7
 */
public class BaseEnvironmentInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
        BaseEnvironment.setApplicationContext(applicationContext);
    }
}
