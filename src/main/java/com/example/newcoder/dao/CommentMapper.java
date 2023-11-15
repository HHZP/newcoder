package com.example.newcoder.dao;

import com.example.newcoder.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {


    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType, int entityId);

    int insertComment(Comment comment);

    @Select("select id, user_id, entity_type, entity_id, target_id, content, status, create_time from comment where id = #{id}")
    Comment selectCommentById(int id);
}
