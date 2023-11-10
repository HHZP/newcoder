package com.example.newcoder.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    //替换符号
    private static final String REPLACEMENT = "***";

    //根节点
    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init(){
        try(
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while((keyword = reader.readLine()) != null){
                this.addKeyword(keyword);
            }
        }catch (IOException e) {
            logger.error("加载敏感词文件失败: " + e.getMessage());
        }

    }

    private void addKeyword(String keyword) {
        TrieNode tempNode = rootNode;
        for(int i=0; i<keyword.length();i++){
            char c= keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);

            if (subNode == null) {
                subNode = new TrieNode();
                tempNode.addSubNode(c,subNode);
            }

            //指向子节点,进行下一轮循环
            tempNode = subNode;

            //设置结束标志
            if (i == keyword.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }
    /*
    * 过滤敏感词
    * */
    public String filter(String text) {
        if(StringUtils.isBlank(text)) {
            return null;
        }
        //指针1
        TrieNode tempNode = rootNode;
        //指针2
        int begin = 0;
        //指针3
        int position = 0;
        StringBuilder sb = new StringBuilder();

        while (begin < text.length()) {

            if(position >= text.length()){
                sb.append(text.charAt(begin));
                position = ++begin;
                tempNode = rootNode;
                continue;
            }
            char c = text.charAt(position);

            //跳过符号
            if (isSymbol(c)) {
                if(tempNode == rootNode) {
                    begin++;
                    sb.append(c);
                }
                position++;
                continue;
            }

            tempNode = tempNode.getSubNode(c);
            if(tempNode == null) {
                sb.append(text.charAt(begin));
                position = ++begin;
                tempNode = rootNode;
            } else if(tempNode.isKeywordEnd()){
                sb.append(REPLACEMENT);
                tempNode= rootNode;
                begin = ++position;
            } else {
                ++position;
            }
        }
        return sb.toString();
    }

    //判断是不是特殊符号
    //从0x2E80~0X9FFF是东亚文字范围
    private boolean isSymbol(Character c) {
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    //前缀树
    private class TrieNode {

        //关键词结束标识
        private boolean isKeywordEnd = false;

        //子节点 char..是下级字符 trieNode是下一个节点
        private Map<Character,TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        // 添加子节点
        public void addSubNode(Character c,TrieNode node) {
            subNodes.put(c,node);
        }

        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }
}
