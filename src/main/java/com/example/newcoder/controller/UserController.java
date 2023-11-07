package com.example.newcoder.controller;

import com.example.newcoder.annotation.LoginRequired;
import com.example.newcoder.entity.User;
import com.example.newcoder.service.UserService;
import com.example.newcoder.util.CommunityUtil;
import com.example.newcoder.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @GetMapping("/setting")
    public String getSettingPage() {
        return "/site/setting";
    }

    @LoginRequired
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage, Model model){
        if(headerImage == null) {
            model.addAttribute("error","你还没有选择图片!");
            return "/site/setting";
        }

        String fileName = headerImage.getOriginalFilename();//获得原始文件名
        String suffix = fileName.substring(fileName.lastIndexOf("."));  //获取文件后缀
        if(StringUtils.isBlank(suffix)) {
            model.addAttribute("error","文件的格式不正确");
            return "/site/setting";
        }

        //生成随机文件名
        fileName = CommunityUtil.generateUUID() + suffix;
        File dest = new File(uploadPath + "/" +fileName);
        try{
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败: " + e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常!",e);
        }

        // 更新当前用户的头像的路径
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(),headerUrl);

        return "redirect:/index";
    }

    @GetMapping("/header/{fileName}")
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        fileName = uploadPath + "/" +fileName;
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //设置格式
        response.setContentType("image/" +suffix);
        try(
                OutputStream os = response.getOutputStream();
                FileInputStream fis = new FileInputStream(fileName);
          ) {
            byte[] buffer = new byte[1024];
            int b=0;
            while ((b=fis.read(buffer)) != -1){
                os.write(buffer,0,b);
            }
        } catch (IOException e){
            logger.error("读取头像失败: " + e.getMessage());
        }
    }
}