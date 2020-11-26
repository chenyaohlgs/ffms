package com.hisense.ffms.dao;

import com.hisense.ffms.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.context.annotation.Bean;

@Mapper
public interface UserDao {

    @Select("select * from user where id = #{id}")
    public User getUserById(Integer id);
}
