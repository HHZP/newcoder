package com.example.newcoder;

import com.example.newcoder.controller.HelloController;
import com.example.newcoder.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NewcoderApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(NewcoderApplicationTests.class);

    @Autowired
    private int a;

    @Autowired
    SensitiveFilter sensitiveFilter;
    @Test
    void contextLoads() {
        String text = "赌博|||||111，嫖娼，开票";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }

}
