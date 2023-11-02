package com.example.newcoder.entity;

import lombok.Data;

import java.util.Date;

//帖子
@Data
public class DiscussPost {
    private int id;
    private int userId;
    private String title;
    private String content;
    private int type;
    private int status;
    private Date createTime;
    private int commentCount;
    private double score;
}
