package top.kelton.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @description:
 * @author: zzk
 * @create: 2024-10-29 14:59
 **/
@EnableScheduling
@SpringBootApplication
public class TestMain {

    public static void main(String[] args) {
        SpringApplication.run(TestMain.class, args);
    }
}
