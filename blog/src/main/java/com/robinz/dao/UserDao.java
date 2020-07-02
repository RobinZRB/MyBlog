package com.robinz.dao;

import com.robinz.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDao {
    //从Mapping全局配置中我们配置好了这个方法，Service层可以直接调用
    User queryByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}
