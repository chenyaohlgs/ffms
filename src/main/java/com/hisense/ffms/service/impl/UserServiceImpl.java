package com.hisense.ffms.service.impl;

import com.hisense.ffms.bean.User;
import com.hisense.ffms.dao.UserDao;
import com.hisense.ffms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getUserById(Integer Id) {
        return userDao.getUserById(Id);
    }

    @Override
    public User getUserByByUsername(String username) {
        return userDao.getUserByByUsername(username);
    }
}
