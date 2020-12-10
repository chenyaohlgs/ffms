package com.hisense.ffms.utils;

import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Data
public class Result {

    private Boolean success; //是否成功

    private Integer code; //返回状态码

    private String message; //返回消息

    private Map<String,Object> data = new HashMap<String, Object>();

    private Result(){

    }

    public static Result ok(){
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(20000);
        result.setMessage("请求处理成功");
        return result;
    }

    public static Result error(){
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(50000);
        result.setMessage("请求处理失败");
        return result;
    }

    public Result success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public Result message(String message){
        this.setMessage(message);
        return this;
    }

    public Result code(Integer code){
        this.setCode(code);
        return this;
    }

    public Result data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public Result data(Map<String, Object> map){
        this.setData(map);
        return this;
    }
}
