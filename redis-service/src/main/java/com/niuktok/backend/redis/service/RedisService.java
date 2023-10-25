package com.niuktok.backend.redis.service;

public interface RedisService {
    /**
     * 获取缓存
     * @param key
     * @return
     */
    Object get(String key);
}