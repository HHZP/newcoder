package com.example.newcoder.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import org.springframework.data.annotation.Id;
import java.util.Date;

//帖子
@Data
@Document(indexName = "discusspost", createIndex = true)
public class DiscussPost {
    @Id
    private int id;

    @Field(type = FieldType.Integer)
    private int userId;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String content;

    @Field(type = FieldType.Integer)
    private int type; //0-普通, 1-置顶

    @Field(type = FieldType.Integer)
    private int status; //0-正常, 1-精华, 2-拉黑

    @Field(type = FieldType.Date)
    private Date createTime;

    @Field(type = FieldType.Integer)
    private int commentCount;

    @Field(type = FieldType.Double)
    private double score;
}
