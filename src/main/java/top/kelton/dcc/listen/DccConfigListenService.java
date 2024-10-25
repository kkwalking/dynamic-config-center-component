package top.kelton.dcc.listen;

/**
 * @description: DCC动态配置监听服务
 * @author: zzk
 * @create: 2024-10-25 16:13
 **/
public interface DccConfigListenService {
    String findByKey(String key);
    void insertConfig(String key, String value);
}
