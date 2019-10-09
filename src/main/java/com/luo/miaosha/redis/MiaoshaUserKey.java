package com.luo.miaosha.redis;

public class MiaoshaUserKey extends BaseKeyPrefix {
    public MiaoshaUserKey() {
    }

    public MiaoshaUserKey(String prefix) {
        super(prefix);
    }

    public MiaoshaUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoshaUserKey token = new MiaoshaUserKey("tk");
    public static MiaoshaUserKey getById = new MiaoshaUserKey("id");
}
