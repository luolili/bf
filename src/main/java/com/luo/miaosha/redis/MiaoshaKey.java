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
    public static MiaoshaKey getMiaoshaPath = new MiaoshaKey(60, "mpath");
    public static MiaoshaKey getVefiryCode = new MiaoshaKey(600, "vc");
}
