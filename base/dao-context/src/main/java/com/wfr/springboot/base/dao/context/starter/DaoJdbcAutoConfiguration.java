package com.wfr.springboot.base.dao.context.starter;

import com.wfr.springboot.base.dao.context.jdbc.JdbcTemplateExtensionConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcProperties;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * jdbc模块自动装配
 *
 * @author wangfarui
 * @see JdbcTemplateAutoConfiguration
 * @since 2022/7/25
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({DataSource.class, JdbcTemplate.class})
@ConditionalOnSingleCandidate(DataSource.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@AutoConfigureBefore(JdbcTemplateAutoConfiguration.class)
@EnableConfigurationProperties(JdbcProperties.class)
@Import(JdbcTemplateExtensionConfiguration.class)
public class DaoJdbcAutoConfiguration {


}
