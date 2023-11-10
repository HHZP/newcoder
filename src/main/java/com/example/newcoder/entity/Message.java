package com.example.newcoder.entity;


import lombok.Data;

import java.util.Date;

@Data
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String conversationId; //111-112
    private String content;
    private int status; //0-未读 1-已读 2-删除
    private Date createTime;
}
