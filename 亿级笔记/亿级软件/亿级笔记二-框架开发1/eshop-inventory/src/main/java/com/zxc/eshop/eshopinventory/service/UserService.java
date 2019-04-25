package com.zxc.eshop.eshopinventory.service;

import com.zxc.eshop.eshopinventory.model.User;

/**
 * 用户Service接口
 */
public interface UserService {

    /**
     * 查询测试用户的信息
     * @return
     */
    public User findUserInfo();

    public User getCachedUserInfo();
}
