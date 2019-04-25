package com.zxc.eshop.eshopinventory.mapper;

import com.zxc.eshop.eshopinventory.model.User;

/**
 * 测试用户的Mapper
 */
public interface UserMapper {

    /**
     * 查询测试用户的信息
     * @return
     */
    public User findUserInfo();
}
