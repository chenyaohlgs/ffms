package com.hisense.ffms.service.impl;

import com.hisense.ffms.dao.RoleDao;
import com.hisense.ffms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public String getRoleNameById(Integer id) {
        return roleDao.getRoleNameById(id);
    }
}
