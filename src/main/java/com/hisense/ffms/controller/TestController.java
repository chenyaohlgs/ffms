package com.hisense.ffms.controller;

import com.hisense.ffms.bean.User;
import com.hisense.ffms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private UserService userService;

    @GetMapping("getUserInfo/{id}")
    public User getUserInfo(@PathVariable("id") Integer id){
        return userService.getUserById(id);
    }
}
