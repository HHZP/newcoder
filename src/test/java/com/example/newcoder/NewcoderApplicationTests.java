package com.example.newcoder;

import com.example.newcoder.controller.HelloController;
import com.example.newcoder.dao.DiscussPostMapper;
import com.example.newcoder.entity.DiscussPost;
import com.example.newcoder.util.SensitiveFilter;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

@SpringBootTest
class NewcoderApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(NewcoderApplicationTests.class);

    @Autowired
    private DiscussPostMapper discussPostMapper;


}
