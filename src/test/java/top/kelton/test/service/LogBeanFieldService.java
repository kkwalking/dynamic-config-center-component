package top.kelton.test.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.kelton.test.domain.TestDccObj;

import javax.annotation.Resource;

/**
 * @description:
 * @author: zzk
 * @create: 2024-10-29 15:01
 **/
@Service
public class LogBeanFieldService {

    @Resource
    private TestDccObj testDccObj;

    @Scheduled(cron = "0/10 * * * * ?")
    public void logBeanField() {
        System.out.println("当前值:" + testDccObj.getName());
    }
}
