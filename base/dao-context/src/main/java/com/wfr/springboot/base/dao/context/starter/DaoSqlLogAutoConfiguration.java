package com.wfr.springboot.base.dao.context.starter;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.wfr.springboot.base.dao.context.mybatis.MyBatisSqlInterceptor;
import com.wfr.springboot.base.dao.context.properties.DaoSqlLogProperties;
import com.wfr.springboot.base.log.context.service.AbstractLogService;
import com.wfr.springboot.base.log.context.starter.LogServiceAutoConfiguration;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 数据访问层 SQL 日志自动装配
 *
 * @author wangfarui
 * @since 2022/7/27
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean({AbstractLogService.class})
@ConditionalOnProperty(prefix = "dao.sql.log", name = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter({LogServiceAutoConfiguration.class})
@EnableConfigurationProperties(DaoSqlLogProperties.class)
public class DaoSqlLogAutoConfiguration {


    /**
     * mybatis sql 日志自动装配
     * <br/><b>主要是为了注入 {@link Interceptor} 拦截器</b>
     * <p>
     * <br/>{@link SqlSessionFactory} 从 {@link MybatisAutoConfiguration} 和 {@link MybatisPlusAutoConfiguration} 选其一.
     * 两者都存在时, 根据加载顺序任选其一.
     * 两者都不存在时, {@link Interceptor} 将无法注入, mybatis相关的sql日志则失效.
     *
     * <p>reference from {@link org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration}</p>
     * <p>reference from {@link com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration}</p>
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
    @AutoConfigureBefore(name = {
            "org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration",
            "com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration"})
    @Import({MyBatisSqlInterceptor.class})
    static class MyBatisSqlLogAutoConfiguration {

    }
}
