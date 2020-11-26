package com.hisense.ffms.bean;

import lombok.Data;


@Data
public class Privilege {
    private Integer ID; //主键
    private String privilegeNumber; // 权限编号
    private String privilegeName; // 权限名称
    private Character privilegeTipFlag; // 菜单级别
    private Character privilegeTypeFlag; // 权限启用标识
}
