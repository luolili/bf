package com.luo.miaosha.redis;

public class GoodsKey extends BaseKeyPrefix {
    public GoodsKey() {
    }

    public GoodsKey(String prefix) {
        super(prefix);
    }

    public GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey("gl");
    public static GoodsKey getMiaoshaGoodsStock = new GoodsKey("gs");
}
