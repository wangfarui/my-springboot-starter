package com.wfr.springboot.base.dao.context.mybatis;

import com.mysql.cj.jdbc.ClientPreparedStatement;
import com.wfr.springboot.base.dao.context.properties.DaoSqlLogProperties;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * 基于 Mysql 数据源的SQL拦截器
 *
 * @author wangfarui
 * @since 2022/7/26
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})
})
public class MysqlDatasourceMyBatisSqlInterceptor extends MyBatisSqlInterceptor {

    public MysqlDatasourceMyBatisSqlInterceptor(DaoSqlLogProperties sqlLogProperties) {
        super(sqlLogProperties);
    }

    @Override
    protected String getNativeSql(Object statement) throws SQLException {
        if (statement instanceof ClientPreparedStatement) {
            ClientPreparedStatement clientPreparedStatement = (ClientPreparedStatement) statement;
            return clientPreparedStatement.asSql();
        }
        return null;
    }
}