package com.hisense.ffms.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RoleDao {

    @Select("select rolename from role where roleid = #{id}")
    public String getRoleNameById(Integer id);
}
