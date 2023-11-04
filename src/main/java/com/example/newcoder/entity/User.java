package com.example.newcoder.entity;

import lombok.Data;

import java.util.Date;

//用户
@Data
public class User {
    private int id;
    private String username;
    private String password;
    private String salt; //随机字符串，用于给密码加密
    private String email;
    private int type;
    private int status;
    private String activationCode;
    private String headerUrl;
    private Date createTime;
}
