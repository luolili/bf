package com.luo.miaosha.service;

import com.luo.miaosha.dao.UserMapper;
import com.luo.miaosha.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User getUserById(Integer id) {
        User user=userMapper.getUserById(id);
        return user;
    }
}
