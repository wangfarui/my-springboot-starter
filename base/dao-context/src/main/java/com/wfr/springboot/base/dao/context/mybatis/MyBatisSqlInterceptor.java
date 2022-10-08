package com.wfr.springboot.base.dao.context.mybatis;

import com.mysql.cj.jdbc.ClientPreparedStatement;
import com.wfr.springboot.base.dao.context.SqlMethod;
import com.wfr.springboot.base.dao.context.log.SqlLogKeyConstants;
import com.wfr.springboot.base.dao.context.properties.DaoSqlLogProperties;
import com.wfr.springboot.base.log.context.LogData;
import com.wfr.springboot.base.log.context.LogLever;
import com.wfr.springboot.base.log.context.LogService;
import com.wfr.springboot.base.log.context.Logger;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;

import java.sql.Statement;

/**
 * MyBatis 的SQL拦截器
 * <p>主要拦截 {@code insert}、{@code delete}、{@code update}、{@code select} 语句</p>
 *
 * @author wangfarui
 * @since 2022/7/26
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})
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
        Object[] args = invocation.getArgs();

        LogData logData = LogData.emptyLogData();
        boolean isException = false;
        try {
            Object statement = args[0];
            if (statement instanceof ClientPreparedStatement) {
                ClientPreparedStatement clientPreparedStatement = (ClientPreparedStatement) statement;
                String sql = clientPreparedStatement.asSql().replaceAll("[\\s]+", " ").trim();
                logData = LogData.createLogData(this.normalSqlLogLevel)
                        .add(SqlLogKeyConstants.SQL_METHOD, getSqlMethod(sql))
                        .add(SqlLogKeyConstants.SQL_CONTENT, sql);
            }
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