package top.kelton.dcc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description: DCC元配置信息
 * @author: zzk
 * @create: 2024-10-25 16:03
 **/

@Component
@ConfigurationProperties(prefix = "dcc")
public class DccConfigProperties {

    private String type;

    private MetaConfig metaConfig;

    private LogConfig logConfig;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MetaConfig getMetaConfig() {
        return metaConfig;
    }

    public void setMetaConfig(MetaConfig metaConfig) {
        this.metaConfig = metaConfig;
    }

    public LogConfig getLogConfig() {
        return logConfig;
    }

    public void setLogConfig(LogConfig logConfig) {
        this.logConfig = logConfig;
    }

    public static class MetaConfig {
        private String table;
        private String keyField;
        private String valueField;

        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            this.table = table;
        }

        public String getKeyField() {
            return keyField;
        }

        public void setKeyField(String keyField) {
            this.keyField = keyField;
        }

        public String getValueField() {
            return valueField;
        }

        public void setValueField(String valueField) {
            this.valueField = valueField;
        }
    }

    public static class LogConfig {
        private String enable;

        public String getEnable() {
            return enable;
        }

        public void setEnable(String enable) {
            this.enable = enable;
        }
    }
}
