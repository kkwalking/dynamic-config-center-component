package top.kelton.test.domain;

import lombok.Data;
import org.springframework.stereotype.Component;
import top.kelton.dcc.annotation.DccValue;

/**
 * @description: 测试使用的使用dcc的bean
 * @author: zzk
 * @create: 2024-10-29 14:58
 **/

@Data
@Component
public class TestDccObj {

    @DccValue("name:zzk")
    private String name;
}
