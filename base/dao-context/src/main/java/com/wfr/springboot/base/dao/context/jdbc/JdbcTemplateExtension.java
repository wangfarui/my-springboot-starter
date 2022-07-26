package com.wfr.springboot.base.dao.context.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.sql.DataSource;
import java.util.List;

/**
 * {@link JdbcTemplate} 扩展类
 *
 * @author wangfarui
 * @since 2022/7/25
 */
public class JdbcTemplateExtension extends JdbcTemplate {

    public JdbcTemplateExtension() {
        super();
    }

    public JdbcTemplateExtension(DataSource dataSource) {
        super(dataSource);
    }

    public JdbcTemplateExtension(DataSource dataSource, boolean lazyInit) {
        super(dataSource, lazyInit);
    }

    @Nullable
    @Override
    public <T> T query(@NonNull String sql, @NonNull ResultSetExtractor<T> rse) throws DataAccessException {
        return super.query(sql, rse);
    }

    @Override
    public void query(@NonNull String sql, @NonNull RowCallbackHandler rch) throws DataAccessException {
        super.query(sql, rch);
    }

    @Override
    public @NonNull <T> List<T> query(@NonNull String sql, @NonNull RowMapper<T> rowMapper) throws DataAccessException {
        return super.query(sql, rowMapper);
    }
}
