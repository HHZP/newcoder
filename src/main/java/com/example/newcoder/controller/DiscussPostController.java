package com.example.newcoder.controller;

import com.example.newcoder.dao.DiscussPostMapper;
import com.example.newcoder.entity.Comment;
import com.example.newcoder.entity.DiscussPost;
import com.example.newcoder.entity.Page;
import com.example.newcoder.entity.User;
import com.example.newcoder.service.CommentService;
import com.example.newcoder.service.DiscussPostService;
import com.example.newcoder.service.LikeService;
import com.example.newcoder.service.UserService;
import com.example.newcoder.util.CommunityConstant;
import com.example.newcoder.util.CommunityUtil;
import com.example.newcoder.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @PostMapping("/add")
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403,"你还没有登录!"); //403表示没有权限
        }

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);

        // 报错的情况,将来统一处理.
        return CommunityUtil.getJSONString(0, "发布成功!");
    }

    //查询帖子详情
    @GetMapping("/detail/{disscussPostId}")
    public String getDiscussPost(@PathVariable("disscussPostId") int disscussPostId, Model model, Page page){

        //帖子
        DiscussPost post = discussPostService.findDiscussPostById(disscussPostId);
        model.addAttribute("post",post);

        //作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user",user);

        //点赞数量
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST,disscussPostId);
        model.addAttribute("likeCount",likeCount);

        //点赞状态
        int likeStatus = hostHolder.getUser() == null ? 0 :
                likeService.findEntityLikeStatus(hostHolder.getUser().getId(),ENTITY_TYPE_POST,disscussPostId);
        model.addAttribute("likeStatus",likeStatus);

        //评论
        page.setLimit(5);
        page.setPath("/discuss/detail/" + disscussPostId);
        page.setRows(post.getCommentCount());

        //给帖子回复的列表
        List<Comment> commentList = commentService.findCommentsByEntity(ENTITY_TYPE_POST, post.getId(),page.getOffset(),page.getLimit());

        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                // 评论VO
                Map<String, Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment", comment);
                // 作者
                commentVo.put("user", userService.findUserById(comment.getUserId()));

                //点赞数量
                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,comment.getId());
                commentVo.put("likeCount",likeCount);

                //点赞状态
                likeStatus = hostHolder.getUser() == null ? 0 :
                        likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeStatus",likeStatus);

                // 回复列表
                List<Comment> replyList = commentService.findCommentsByEntity(
                        ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                // 回复VO列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        // 回复
                        replyVo.put("reply", reply);
                        // 作者
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        // 回复目标
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);

                        //点赞数量
                        likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST,reply.getId());
                        replyVo.put("likeCount",likeCount);
                        //点赞状态
                        likeStatus = hostHolder.getUser() == null ? 0 :
                                likeService.findEntityLikeStatus(hostHolder.getUser().getId(),ENTITY_TYPE_POST,reply.getId());
                        replyVo.put("likeStatus",likeStatus);


                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);

                // 回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);

                commentVoList.add(commentVo);
            }
        }

        model.addAttribute("comments",commentVoList);

        return "/site/discuss-detail";
    }
}
