package com.hisense.ffms.controller;

import com.hisense.ffms.bean.User;
import com.hisense.ffms.service.RoleService;
import com.hisense.ffms.service.UserService;
import com.hisense.ffms.utils.JwtUtil;
import com.hisense.ffms.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@Controller
@Slf4j
public class TestController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @ResponseBody
    @GetMapping("user/info")
    public Result getUserInfo(HttpServletRequest request){ // 根据token获取用户信息
        log.info("调用获取info方法");
        User user = userService.getUserByByUsername(JwtUtil.getUserIdByRequest(request));
        String roleName = roleService.getRoleNameById(user.getRoleId());
        return Result.ok().data("name",user.getRealName()).data("role",roleName);
    }

    @ResponseBody
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        User resouceUser = userService.getUserByByUsername(user.getUsername());
        if (resouceUser == null){
            return Result.error().message("用户名不存在");
        }
        if (!user.getPassword().equals(resouceUser.getPassword())){
            return Result.error().message("密码不正确");
        }
        return Result.ok().data("token", JwtUtil.encode(resouceUser.getUsername(),resouceUser.getID().toString()));
    }

    @ResponseBody
    @PostMapping("user/logout")
    public Result logout(){
        log.info("执行logout方法");
        return Result.ok().data("token",null);
    }
}
