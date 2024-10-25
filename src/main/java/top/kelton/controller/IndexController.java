package top.kelton.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.kelton.dcc.annotation.DccValue;

/**
 * @description:
 * @author: zzk
 * @create: 2024-10-25 10:16
 **/
@Slf4j
@RestController
public class IndexController {
    @DccValue("dccSample:true")
    private String dccSample = "true";


    @GetMapping("/showDcc")
    public String getDccValue() {
        log.info("[showDcc]dccSample:{}",dccSample);
        return dccSample;

    }
}
