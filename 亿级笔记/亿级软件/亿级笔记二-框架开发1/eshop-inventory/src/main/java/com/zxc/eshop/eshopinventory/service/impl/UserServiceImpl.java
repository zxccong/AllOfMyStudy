package com.zxc.eshop.eshopinventory.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zxc.eshop.eshopinventory.dao.RedisDao;
import com.zxc.eshop.eshopinventory.mapper.UserMapper;
import com.zxc.eshop.eshopinventory.model.User;
import com.zxc.eshop.eshopinventory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisDao redisDao;

    @Override
    public User findUserInfo() {
        return userMapper.findUserInfo();
    }

    @Override
    public User getCachedUserInfo() {
        redisDao.set("cached_user", "{\"name\": \"zhangsan\", \"age\": 25}") ;
        String json = redisDao.get("cached_user");
        JSONObject jsonObject = JSONObject.parseObject(json);

        User user = new User();
        user.setName(jsonObject.getString("name"));
        user.setAge(jsonObject.getInteger("age"));

        return user;
    }


}
