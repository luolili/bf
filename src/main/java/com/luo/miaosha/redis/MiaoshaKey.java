package com.luo.miaosha.redis;

public class MiaoshaKey extends BaseKeyPrefix {
    public MiaoshaKey() {
    }

    public MiaoshaKey(String prefix) {
        super(prefix);
    }

    public MiaoshaKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoshaKey isGoodsOVer = new MiaoshaKey("go");
}
