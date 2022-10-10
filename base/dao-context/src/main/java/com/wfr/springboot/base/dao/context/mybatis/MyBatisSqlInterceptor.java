package com.wfr.springboot.base.dao.context.mybatis;

import com.wfr.base.framework.common.utils.StringUtils;
import com.wfr.springboot.base.dao.context.SqlMethod;
import com.wfr.springboot.base.dao.context.log.SqlLogKeyConstants;
import com.wfr.springboot.base.dao.context.properties.DaoSqlLogProperties;
import com.wfr.springboot.base.log.context.LogData;
import com.wfr.springboot.base.log.context.LogLever;
import com.wfr.springboot.base.log.context.LogService;
import com.wfr.springboot.base.log.context.Logger;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;

import java.sql.SQLException;

/**
 * MyBatis 的SQL拦截器
 * <p>主要拦截 {@code insert}、{@code delete}、{@code update}、{@code select} 语句</p>
 *
 * @author wangfarui
 * @since 2022/7/26
 */
public abstract class MyBatisSqlInterceptor implements Interceptor {

    /**
     * 正常sql日志级别
     */
    protected final LogLever normalSqlLogLevel;

    /**
     * 慢sql日志级别
     */
    protected final LogLever slowSqlLogLevel;

    /**
     * 异常sql日志级别
     */
    protected final LogLever errorSqlLogLevel;

    /**
     * 慢sql时间
     */
    protected final long slowSqlTime;

    /**
     * 主日志服务
     */
    protected final LogService logService;

    public MyBatisSqlInterceptor(DaoSqlLogProperties sqlLogProperties) {
        DaoSqlLogProperties properties = sqlLogProperties != null ? sqlLogProperties : new DaoSqlLogProperties();
        this.normalSqlLogLevel = properties.getNormalSqlLogLevel();
        this.slowSqlLogLevel = properties.getSlowSqlLogLevel();
        this.errorSqlLogLevel = properties.getErrorSqlLogLevel();
        this.slowSqlTime = properties.getSlowSqlTime().toMillis();
        this.logService = Logger.defaultLogService();
    }

    /**
     * 获取原生sql语句
     *
     * @param statement SQL Statement
     * @return sql
     */
    protected abstract String getNativeSql(Object statement) throws SQLException;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();

        LogData logData = LogData.emptyLogData();
        boolean isException = false;
        try {
            Object statement = args[0];
            String nativeSql = getNativeSql(statement);
            if (StringUtils.isBlank(nativeSql)) {
                return invocation.proceed();
            }
            String sql = nativeSql.replaceAll("[\\s]+", " ").trim();
            logData = LogData.createLogData(this.normalSqlLogLevel)
                    .add(SqlLogKeyConstants.SQL_METHOD, getSqlMethod(sql))
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

    private static SqlMethod getSqlMethod(String sql) {
        if (sql.length() < 6) {
            return SqlMethod.UNKNOWN;
        }
        String sqlPrefix = sql.substring(0, 6).toUpperCase();
        if (sqlPrefix.startsWith("SELECT")) {
            return SqlMethod.SELECT;
        } else if (sqlPrefix.startsWith("UPDATE")) {
            return SqlMethod.UPDATE;
        } else if (sqlPrefix.startsWith("INSERT")) {
            return SqlMethod.INSERT;
        } else if (sqlPrefix.startsWith("DELETE")) {
            return SqlMethod.DELETE;
        }
        return SqlMethod.UNKNOWN;
    }

}