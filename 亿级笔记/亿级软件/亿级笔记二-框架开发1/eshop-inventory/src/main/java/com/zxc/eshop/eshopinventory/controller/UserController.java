package com.zxc.eshop.eshopinventory.controller;

import com.zxc.eshop.eshopinventory.model.User;
import com.zxc.eshop.eshopinventory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping("/getUserInfo")
    @ResponseBody
    public User getUserInfo(){
        User user= userService.findUserInfo();
        return  user;
    }

    @RequestMapping("/getCachedUserInfo")
    @ResponseBody
    public User getCachedUserInfo() {
        User user = userService.getCachedUserInfo();
        return user;
    }



}
