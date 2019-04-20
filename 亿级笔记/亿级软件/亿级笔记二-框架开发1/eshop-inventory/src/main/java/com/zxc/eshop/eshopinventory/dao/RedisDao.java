package com.zxc.eshop.eshopinventory.dao;

/**
 * redis
 */
public interface RedisDao {

    void set(String key,String value);

    String get(String key);

    void delete(String key);
}
