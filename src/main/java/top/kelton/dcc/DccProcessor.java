package top.kelton.dcc;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import top.kelton.dcc.annotation.DccValue;
import top.kelton.dcc.config.DccConfigProperties;
import top.kelton.dcc.listen.db.DatabaseDccConfigListenService;

import javax.annotation.PreDestroy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @description: 动态配置处理器
 * @author: zzk
 * @create: 2024-10-25 10:45
 **/
@Slf4j
@Configuration
@EnableConfigurationProperties(DccConfigProperties.class)
public class DccProcessor implements BeanPostProcessor {
    /**
     * 内部类，用于存储 bean 和对应的字段
     */
    private static class BeanField {
        private final Object bean;
        private final Field field;

        public BeanField(Object bean, Field field) {
            this.bean = bean;
            this.field = field;
        }

        public Object getBean() {
            return bean;
        }

        public Field getField() {
            return field;
        }
    }

    private final DatabaseDccConfigListenService databaseDccConfigListenService;
    private final ScheduledExecutorService scheduler;

    private final Map<String, List<BeanField>> beanMap = new ConcurrentHashMap<>();

    public DccProcessor(DatabaseDccConfigListenService databaseDccConfigListenService) {
        this.databaseDccConfigListenService = databaseDccConfigListenService;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();

        // 每3秒从dccDBService获取配置值，设置到对应的属性值中
        this.scheduler.scheduleAtFixedRate(this::updateConfigValues, 0, 3, TimeUnit.SECONDS);
    }

    /**
     * 定时任务方法，用于更新配置值并设置到相应的 bean 属性中
     */
    private void updateConfigValues() {
        try {
            for (Map.Entry<String, List<BeanField>> entry : beanMap.entrySet()) {
                String dccKey = entry.getKey();
                List<BeanField> beanFields = entry.getValue();
                String configValue = databaseDccConfigListenService.findByKey(dccKey);

                if (configValue == null) {
                    continue;
                }

                for (BeanField bf : beanFields) {
                    try {
                        Field field = bf.getField();

                        String oldValue = (String)field.get(bf.getBean());
                        if (!oldValue.equals(configValue)) {
                            field.setAccessible(true);
                            field.set(bf.getBean(), configValue);
                            log.info("成功更新DCC属性 key:{}, old value:{}, new value:{}", dccKey, oldValue, configValue);
                        }
                    } catch (IllegalAccessException e) {
                        // 记录日志或其他异常处理
                        throw new RuntimeException("Failed to set DCC config value for bean: "
                                + bf.getBean().getClass().getName(), e);
                    }
                }
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 确保在应用关闭时关闭调度器
     */
    @PreDestroy
    public void shutdown() {
        scheduler.shutdown();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 从数据库获取配置
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(DccValue.class)) {
                // 从数据库获取值，否则设置默认值 null
                DccValue annotation = field.getAnnotation(DccValue.class);
                if (StringUtils.isBlank(annotation.value())) {
                    throw new RuntimeException("Dcc annotation value is required");
                }
                String[] splits = annotation.value().split(":");
                String dccKey = splits[0];
                String dccValue = splits.length > 1 ? splits[1] : null;

                try {
                    // 优先使用数据库配置的值
                    String configValue = databaseDccConfigListenService.findByKey(dccKey);
                    if (configValue != null) {
                        field.setAccessible(true);
                        field.set(bean, configValue);
                    } else {
                        // 使用默认值
                        if (dccValue == null) {
                            throw new RuntimeException(String.format("Dcc config [%s] 's default value is required", dccKey));
                        }
                        // 将默认值添加到数据库
                        databaseDccConfigListenService.insertConfig(dccKey, dccValue);
                        field.setAccessible(true);
                        field.set(bean, dccValue);
                        field.setAccessible(false);
                    }

                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                // 将 bean 和 field 注册到 beanMap 中以便后续更新
                beanMap.computeIfAbsent(dccKey, k -> new ArrayList<>()).add(new BeanField(bean, field));

            }
        }
        return bean;
    }
}
