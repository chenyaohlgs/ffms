package com.hisense.ffms.bean;

import lombok.Data;

import java.sql.Date;

@Data
public class Bill {
    private Integer ID; //主键
    private String title;// 收支主题
    private Integer userId;// 用户id
    private Float money;// 收支金额
    private String remark;// 备注
    private Date dateTime; //交易时间
}
