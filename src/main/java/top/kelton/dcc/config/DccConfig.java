package top.kelton.dcc.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import top.kelton.dcc.listen.db.DatabaseDccConfigListenService;
import top.kelton.dcc.processor.DccProcessor;

/**
 * @description: Dcc配置类
 * @author: zzk
 * @create: 2024-10-29 14:51
 **/
@Configuration
@EnableConfigurationProperties(DccConfigProperties.class)
public class DccConfig {
    @Bean
    @ConditionalOnMissingBean
    public DatabaseDccConfigListenService databaseDccConfigListenService(JdbcTemplate jdbcTemplate, DccConfigProperties dccConfigProperties) {
        return new DatabaseDccConfigListenService(jdbcTemplate, dccConfigProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public DccProcessor dccProcessor(DatabaseDccConfigListenService listenService) {
        return new DccProcessor(listenService);
    }
}
