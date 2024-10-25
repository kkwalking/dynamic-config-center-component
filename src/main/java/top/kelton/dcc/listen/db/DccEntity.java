package top.kelton.dcc.listen.db;

import lombok.Data;

/**
 * @description: 动态属性数据库实体
 * @author: zzk
 * @create: 2024-10-25 11:09
 **/
@Data
public class DccEntity {

    private String configKey;

    private String configValue;
}
