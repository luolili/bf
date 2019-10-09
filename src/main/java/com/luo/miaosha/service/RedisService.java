package com.luo.miaosha.service;

import com.alibaba.fastjson.JSON;
import com.luo.miaosha.redis.KeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    public <T> T get(KeyPrefix prefix,String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix()+key;
            String str = jedis.get(realKey);
            return stringToBean(str, clazz);
        } finally {
            returnToPool(jedis);
        }
    }

    public <T> boolean set(KeyPrefix prefix,String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String result = beanToString(value);
            if (result == null) {
                return false;
            }
            String realKey=prefix.getPrefix()+key;
            int seconds = prefix.expireSeconds();
            if (seconds <= 0) {
                 jedis.set(realKey, result);
            }else {
                 jedis.setex(realKey,seconds, result);
            }
            return true;

        }  finally {
            returnToPool(jedis);
        }
    }
    public <T> boolean exist(KeyPrefix prefix,String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey=prefix.getPrefix()+key;
            return jedis.exists(realKey);
        }  finally {
            returnToPool(jedis);
        }
    }

    public boolean delete(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.del(realKey) > 0;
        } finally {
            returnToPool(jedis);
        }
    }
    public <T> Long incr(KeyPrefix prefix,String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey=prefix.getPrefix()+key;
            return jedis.incr(realKey);
        }  finally {
            returnToPool(jedis);
        }
    }
    public <T> Long decr(KeyPrefix prefix,String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey=prefix.getPrefix()+key;
            return jedis.decr(realKey);
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

        if (clazz == String.class) {
            return (String) value;
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
