package com.niuktok.backend.redis.service;

public interface RedisService {
    Object get(String key);

    void set(String key, Object value);

    Boolean exists(String key);

    void expire(String key, Integer expireSeconds);
}