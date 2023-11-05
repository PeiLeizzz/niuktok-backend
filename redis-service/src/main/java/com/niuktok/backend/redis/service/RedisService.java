package com.niuktok.backend.redis.service;

public interface RedisService {
    Object get(String key);

    void set(String key, Object value);

    void set(String key, Object value, Integer expireSeconds);

    Boolean exists(String key);

    void expire(String key, Integer expireSeconds);

    void delete(String key);
}