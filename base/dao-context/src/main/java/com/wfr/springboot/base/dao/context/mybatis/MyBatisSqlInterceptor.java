package com.wfr.springboot.base.dao.context.mybatis;

import com.wfr.springboot.base.dao.context.log.SqlLogKeyConstants;
import com.wfr.springboot.base.dao.context.properties.DaoSqlLogProperties;
import com.wfr.springboot.base.log.context.*;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;

/**
 * MyBatis 的SQL拦截器
 * <p>主要拦截 {@code insert}、{@code delete}、{@code update}、{@code select} 语句</p>
 *
 * @author wangfarui
 * @since 2022/7/26
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
public class MyBatisSqlInterceptor implements Interceptor {

    /**
     * 正常sql日志级别
     */
    private final LogLever normalSqlLogLevel;

    /**
     * 慢sql日志级别
     */
    private final LogLever slowSqlLogLevel;

    /**
     * 异常sql日志级别
     */
    private final LogLever errorSqlLogLevel;

    /**
     * 慢sql时间
     */
    private final long slowSqlTime;

    /**
     * 主日志服务
     */
    private final LogService logService;

    public MyBatisSqlInterceptor(DaoSqlLogProperties sqlLogProperties) {
        this.normalSqlLogLevel = sqlLogProperties.getNormalSqlLogLevel();
        this.slowSqlLogLevel = sqlLogProperties.getSlowSqlLogLevel();
        this.errorSqlLogLevel = sqlLogProperties.getErrorSqlLogLevel();
        this.slowSqlTime = sqlLogProperties.getSlowSqlTime().toMillis();
        this.logService = Logger.defaultLogService();
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        LogData logData = LogData.emptyLogData();
        boolean isException = false;
        try {
            MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];

            // sql参数获取
            Object parameter = null;
            if (invocation.getArgs().length > 1) {
                parameter = invocation.getArgs()[1];
            }

            BoundSql boundSql = mappedStatement.getBoundSql(parameter);
            Configuration configuration = mappedStatement.getConfiguration();
            // 获取真实的sql语句
            String sql = showSql(configuration, boundSql);

            logData = LogData.createLogData(this.normalSqlLogLevel)
                    .add(SqlLogKeyConstants.SQL_METHOD, mappedStatement.getSqlCommandType())
                    .add(SqlLogKeyConstants.SQL_CONTENT, sql);
            return invocation.proceed();
        } catch (Throwable e) {
            if (!logData.isEmptyLogData()) {
                isException = true;
                logData.logLever(this.errorSqlLogLevel);
                logData.addException(e);
            }
            throw e;
        } finally {
            if (!logData.isEmptyLogData()) {
                if (!isException) {
                    long currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - logData.getStartTime() > this.slowSqlTime) {
                        logData.logLever(this.slowSqlLogLevel);
                    }
                }
                this.logService.push(logData);
            }
        }
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private static String getParameterValue(Object obj) {
        String value;
        if (obj instanceof String) {
            value = "'" + obj + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(
                    DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }

    private static String showSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (!parameterMappings.isEmpty() && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration
                    .getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else {
                        //打印出缺失，提醒该参数缺失并防止错位
                        sql = sql.replaceFirst("\\?", "缺失");
                    }
                }
            }
        }
        return sql;
    }
}