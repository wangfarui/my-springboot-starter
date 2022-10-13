package com.wfr.springboot.base.dao.context.starter;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceWrapper;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.mysql.cj.jdbc.ClientPreparedStatement;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.wfr.springboot.base.dao.context.SqlInterceptor;
import com.wfr.springboot.base.dao.context.interceptor.DruidDataSourceSqlInterceptor;
import com.wfr.springboot.base.dao.context.interceptor.HikariDatasourceMyBatisSqlInterceptor;
import com.wfr.springboot.base.dao.context.interceptor.MyBatisSqlInterceptor;
import com.wfr.springboot.base.dao.context.interceptor.MysqlDatasourceMyBatisSqlInterceptor;
import com.wfr.springboot.base.dao.context.properties.DaoSqlLogProperties;
import com.wfr.springboot.base.log.context.service.AbstractLogService;
import com.wfr.springboot.base.log.context.starter.LogServiceAutoConfiguration;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.ClassUtils;

/**
 * 数据访问层 SQL 日志自动装配
 * <p>加载顺序如下: </p>
 * <ol>
 *     <li>DataSource数据源</li>
 *     <li>MyBatis Plugin插件扩展</li>
 * </ol>
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
     * 基于 mybatis 的sql日志自动装配
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
    @ConditionalOnProperty(prefix = "dao.sql.log", name = "mybatis-enabled", havingValue = "true", matchIfMissing = true)
    @AutoConfigureBefore(name = {
            "org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration",
            "com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration"})
    static class MyBatisSqlLogAutoConfiguration {

        private static final String DATA_SOURCE_TYPE_NAME = "com.zaxxer.hikari.HikariDataSource";

        @Bean
        @ConditionalOnClass({ClientPreparedStatement.class})
        @ConditionalOnMissingBean({SqlInterceptor.class, MyBatisSqlInterceptor.class})
        @ConditionalOnBean(DataSourceProperties.class)
        public MyBatisSqlInterceptor myBatisSqlInterceptor(DataSourceProperties dataSourceProperties,
                                                           ObjectProvider<DaoSqlLogProperties> sqlLogProperties) {
            Class<?> type = dataSourceProperties.getType();
            DaoSqlLogProperties daoSqlLogProperties = sqlLogProperties.getIfAvailable();

            // DataSourceProperties.type为空时, 拿Hikari作为默认数据源
            if (type == null) {
                try {
                    type = ClassUtils.forName(DATA_SOURCE_TYPE_NAME, null);
                } catch (ClassNotFoundException e) {
                    // ignore
                    return null;
                }
            }

            if (MysqlDataSource.class.isAssignableFrom(type)) {
                return new MysqlDatasourceMyBatisSqlInterceptor(daoSqlLogProperties);
            } else if (HikariDataSource.class.isAssignableFrom(type)) {
                return new HikariDatasourceMyBatisSqlInterceptor(daoSqlLogProperties);
            }

            // 未知数据源暂时返回NullBean, 代表不采用MyBatisSqlInterceptor打印sql日志
            return null;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(DruidDataSourceWrapper.class)
    @ConditionalOnProperty(prefix = "dao.sql.log", name = "datasource-enabled", havingValue = "true", matchIfMissing = true)
    @AutoConfigureBefore(DruidDataSourceAutoConfigure.class)
    static class DataSourceSqlLogAutoConfiguration {

        @Configuration(proxyBeanMethods = false)
        @ConditionalOnMissingBean(SqlInterceptor.class)
        @ConditionalOnBean(DataSourceProperties.class)
        @Import(DruidDataSourceSqlInterceptor.class)
        static class DataSourceSqlInterceptor {

        }
    }
}
