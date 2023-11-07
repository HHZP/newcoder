package com.example.newcoder.controller;

import com.example.newcoder.entity.DiscussPost;
import com.example.newcoder.entity.Page;
import com.example.newcoder.entity.User;
import com.example.newcoder.service.DiscussPostService;
import com.example.newcoder.service.UserService;
import com.example.newcoder.util.CommunityUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import org.slf4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class HelloController {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @GetMapping("/index")
    public String getIndexPage(Model model,Page page) {
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> list = discussPostService.findDiscussPosts(0,page.getoffset(),page.getLimit());
        List<Map<String,Object>> discussPosts = new ArrayList<>();
        if (list!=null){
            for (DiscussPost post : list){
                Map<String,Object> map = new HashMap<>();
                map.put("post",post);
                User user = userService.findUserById(post.getUserId());
                map.put("user",user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);
        return "/index";
    }

//    @RequestMapping(path = "/cookie/set", method = RequestMethod.GET)
//    @ResponseBody
//    public String setCookie(HttpServletResponse response) {
//        // 创建cookie
//        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
//        // 设置cookie生效的范围
//        //        cookie.setPath("/community/alpha");
//        // 设置cookie的生存时间
//        cookie.setMaxAge(60 * 10);
//        // 发送cookie
//        response.addCookie(cookie);
//
//        return "set cookie";
//    }
//
//    @RequestMapping(path = "/cookie/get", method = RequestMethod.GET)
//    @ResponseBody
//    public String getCookie(@CookieValue("code") String code) {
//        System.out.println(code);
//        return "get cookie";
//    }
//
//    // session示例
//
//    @RequestMapping(path = "/session/set", method = RequestMethod.GET)
//    @ResponseBody
//    public String setSession(HttpSession session) {
//        session.setAttribute("id", 1);
//        session.setAttribute("name", "Test");
//        return "set session";
//    }
//
//    @RequestMapping(path = "/session/get", method = RequestMethod.GET)
//    @ResponseBody
//    public String getSession(HttpSession session) {
//        System.out.println(session.getAttribute("id"));
//        System.out.println(session.getAttribute("name"));
//        return "get session";
//    }

}
