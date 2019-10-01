package com.luo.miaosha.service;

import com.alibaba.fastjson.JSON;
import com.luo.miaosha.dao.UserMapper;
import com.luo.miaosha.domain.User;
import com.luo.miaosha.redis.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    public <T> T get(String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = jedis.get(key);
            T t = stringToBean(str, clazz);
            return t;
        } finally {
            returnToPool(jedis);
        }
    }

    public <T> boolean set(String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String result = beanToString(value);
            if (result == null) {
                return false;
            }
            String str = jedis.set(key, result);
            beanToString(str);
            return true;

        }  finally {
            returnToPool(jedis);
        }
    }

    private <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();

        if (clazz == long.class || clazz == Long.class) {
            return value + "";
        }
        if (clazz == int.class || clazz == Integer.class) {
            return value + "";
        }

        return JSON.toJSONString(value);
    }

    private <T> T stringToBean(String str, Class<T> clazz) {
        if (str == null || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        }
        if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        }
        if (clazz == String.class) {
            return (T) str;
        }
        return JSON.toJavaObject(JSON.parseObject(str), clazz);

    }

    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
           jedisPool.returnResource(jedis);
        }
    }


}
