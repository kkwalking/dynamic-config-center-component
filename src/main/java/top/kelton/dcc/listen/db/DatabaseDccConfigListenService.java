package top.kelton.dcc.listen.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import top.kelton.dcc.config.DccConfigProperties;
import top.kelton.dcc.listen.DccConfigListenService;

import java.util.List;

/**
 * @description: 动态配置数据库服务
 * @author: zzk
 * @create: 2024-10-25 11:10
 **/

public class DatabaseDccConfigListenService implements DccConfigListenService {


    private JdbcTemplate jdbcTemplate;
    private DccConfigProperties dccConfigProperties;

    private String table;
    private String keyField;
    private String valueField;


    public DatabaseDccConfigListenService(JdbcTemplate jdbcTemplate, DccConfigProperties dccConfigProperties) {
        this.jdbcTemplate = jdbcTemplate;
        this.dccConfigProperties = dccConfigProperties;

        // 检查dcc.type是否为db
        if (!"db".equals(dccConfigProperties.getType())) {
            throw new RuntimeException("当前DCC配置类型不为db, 无法使用DatabaseDccConfigListenService");
        }
        table = dccConfigProperties.getMetaConfig().getTable();
        keyField = dccConfigProperties.getMetaConfig().getKeyField();
        valueField = dccConfigProperties.getMetaConfig().getValueField();
        if (table == null || table.isEmpty() ||
                keyField == null || keyField.isEmpty() ||
                valueField == null || valueField.isEmpty()) {
            throw new RuntimeException("当前DCC配置类型为db, 但是配置信息不完善，请检查table, keyField, valueField");
        }
    }

    // 根据key查询value
    @Override
    public String findByKey(String key) {

        // 构造SQL
        String querySql = buildQuerySql();
        RowMapper<DccEntity> rowMapper = (rs, rowNum) -> {
            DccEntity dccEntity = new DccEntity();
            dccEntity.setConfigKey(rs.getString(keyField));
            dccEntity.setConfigValue(rs.getString(valueField));
            return dccEntity;
        };
        List<DccEntity> results = jdbcTemplate.query(querySql, rowMapper, key);
        if (!results.isEmpty()) {
            return results.get(0).getConfigValue();
        }
        return null;
    }

    @Override
    public void insertConfig(String key, String value) {
        String insertSql = buildInsertSql();
        jdbcTemplate.update(insertSql,key, value);

    }

    private String buildQuerySql() {
        String sql = String.format("SELECT %s,%s FROM %s WHERE %s = ?", keyField, valueField, table, keyField);
        return sql;
    }

    private String buildInsertSql() {
        String sql = String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)", table, keyField, valueField);
        return sql;
    }

}
