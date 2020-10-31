package com.robinz.service;

import com.robinz.pojo.User;

public interface UserService {

    public User checkUser(String username, String password);
}
