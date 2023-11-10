package com.example.newcoder;

import com.example.newcoder.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.context.Context;

import java.util.UUID;

import static com.example.newcoder.util.CommunityUtil.getJSONString;

@SpringBootTest
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Test
    public void sendmail(){
        String s = "<script> 111 <script>";
        System.out.println(s);
        System.out.println(getJSONString(1,s));
    }

}
