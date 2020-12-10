package com.hisense.ffms.service;

import com.hisense.ffms.bean.User;

public interface UserService {

    public User getUserById(Integer Id);

    public User getUserByByUsername(String username);
}
