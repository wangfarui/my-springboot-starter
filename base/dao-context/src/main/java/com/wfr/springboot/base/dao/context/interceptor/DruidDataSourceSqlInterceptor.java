package com.wfr.springboot.base.dao.context.interceptor;

import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import com.alibaba.druid.proxy.jdbc.ResultSetProxy;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.wfr.springboot.base.dao.context.SqlConstants;
import com.wfr.springboot.base.dao.context.SqlInterceptor;
import com.wfr.springboot.base.dao.context.properties.DaoSqlLogProperties;
import com.wfr.springboot.base.log.context.LogData;

import java.util.StringJoiner;

/**
 * 基于 Druid 数据源的SQL拦截器
 *
 * @author wangfarui
 * @since 2022/10/12
 */
public class DruidDataSourceSqlInterceptor extends FilterEventAdapter implements SqlInterceptor {

    private final GenericSqlInterceptor sqlInterceptor;

    public DruidDataSourceSqlInterceptor(DaoSqlLogProperties sqlLogProperties) {
        sqlInterceptor = new GenericSqlInterceptor(sqlLogProperties);
    }

    @Override
    protected void statementExecuteUpdateBefore(StatementProxy statement, String sql) {
        pushLogBefore(statement, sql);
    }

    @Override
    protected void statementExecuteUpdateAfter(StatementProxy statement, String sql, int updateCount) {
        pushLogAfter();
    }

    @Override
    protected void statementExecuteQueryBefore(StatementProxy statement, String sql) {
        pushLogBefore(statement, sql);
    }

    @Override
    protected void statementExecuteQueryAfter(StatementProxy statement, String sql, ResultSetProxy resultSet) {
        pushLogAfter();
    }

    @Override
    protected void statementExecuteBefore(StatementProxy statement, String sql) {
        pushLogBefore(statement, sql);
    }

    @Override
    protected void statementExecuteAfter(StatementProxy statement, String sql, boolean result) {
        pushLogAfter();
    }

    @Override
    protected void statementExecuteBatchBefore(StatementProxy statement) {
        // TODO 批量执行语句, 只有最后一个参数值走到了此处
        pushLogBefore(statement, statement.getBatchSql());
    }

    @Override
    protected void statementExecuteBatchAfter(StatementProxy statement, int[] result) {
        pushLogAfter();
    }

    @Override
    protected void statement_executeErrorAfter(StatementProxy statement, String sql, Throwable error) {
        sqlInterceptor.exceptionLog(error, true);
    }

    private void pushLogBefore(StatementProxy statement, String sql) {
        LogData logData = sqlInterceptor.createLog(sql);
        int size = statement.getParametersSize();
        if (size > 0) {
            StringJoiner stringJoiner = new StringJoiner(",");
            for (int i = 0; i < size; i++) {
                JdbcParameter parameter = statement.getParameter(i);
                if (parameter != null) {
                    stringJoiner.add(parameter.getValue().toString());
                }
            }
            logData.add(SqlConstants.SQL_PARAM, stringJoiner.toString());
        }
    }

    private void pushLogAfter() {
        sqlInterceptor.pushLog();
    }
}
