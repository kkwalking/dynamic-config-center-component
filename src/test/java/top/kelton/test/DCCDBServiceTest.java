package top.kelton.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.kelton.Main;
import top.kelton.dcc.listen.db.DatabaseDccConfigListenService;

import javax.annotation.Resource;

/**
 * @description:
 * @author: zzk
 * @create: 2024-10-25 11:19
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Main.class})
public class DCCDBServiceTest {

    @Resource
    private DatabaseDccConfigListenService databaseDccConfigListenService;

    @Test
    public void test_findByKey() {
        String upgrateSwitch = databaseDccConfigListenService.findByKey("upgradeSwitch");
        System.out.println(upgrateSwitch);
    }
}
