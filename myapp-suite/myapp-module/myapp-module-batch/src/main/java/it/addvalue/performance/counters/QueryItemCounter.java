package it.addvalue.performance.counters;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class QueryItemCounter implements ItemCounter {

    private final JdbcTemplate jdbcTemplate;
    private final String sql;
    private final Object[] args;

    public QueryItemCounter(DataSource dataSource, String sql, Object... args) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.sql = sql;
        this.args = args;
    }

    @Override
    public long itemCount() {
        int selectPos = StringUtils.indexOfIgnoreCase(sql, "SELECT");
        String declares = sql.substring(0, selectPos);
        String select = sql.substring(selectPos);
        String countingQuery = declares + "SELECT COUNT(*) FROM (" + select + ") AS total_rows WHERE 1=1";
        return jdbcTemplate.queryForObject(countingQuery, args, Long.class);
    }

}
