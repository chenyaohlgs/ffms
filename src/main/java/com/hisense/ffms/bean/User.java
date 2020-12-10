package com.hisense.ffms.bean;

import lombok.Data;

@Data
public class User {
    private Integer ID; // 用户主键
    private String username; // 用户名
    private String password;// 用户密码
    private String realName; // 用户名真实姓名
    private Integer roleId; // 用户关联角色Id
    private String photo;// 用户照片
}
