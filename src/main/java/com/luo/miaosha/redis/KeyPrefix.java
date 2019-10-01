package com.luo.miaosha.redis;

public interface KeyPrefix {

    int expireSeconds();

    String getPrefix();
}
