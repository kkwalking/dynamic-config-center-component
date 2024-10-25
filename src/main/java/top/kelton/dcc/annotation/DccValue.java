package top.kelton.dcc.annotation;

import java.lang.annotation.*;

/**
 * @description: 动态配置注解
 * @author: zzk
 * @create: 2024-10-25 10:42
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DccValue {

    String value() default "";
}
