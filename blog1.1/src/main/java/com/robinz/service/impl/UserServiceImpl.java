package com.robinz.service.impl;

import com.robinz.dao.UserDao;
import com.robinz.pojo.User;
import com.robinz.service.UserService;
import com.robinz.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service    //注解为可用的service层
public class UserServiceImpl implements UserService {

    @Autowired  //让 spring 完成 bean 自动装配的工作，这样我不需要实例化UserDao就能调用他的方法
    UserDao userDao;

    @Override
    public User checkUser(String username, String password) {
        User user = userDao.queryByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }
}
