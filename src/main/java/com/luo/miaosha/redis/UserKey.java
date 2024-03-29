package com.luo.miaosha.redis;

public class UserKey extends BaseKeyPrefix{
    public UserKey() {
    }

    public UserKey(String prefix) {
        super(prefix);
    }

    public UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
}
