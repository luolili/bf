package com.luo.miaosha.redis;

public class OrderKey extends BaseKeyPrefix {
    public OrderKey() {
    }

    public OrderKey(String prefix) {
        super(prefix);
    }

    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static OrderKey getMiaoshaORderByUidGid = new OrderKey("ug");
}
