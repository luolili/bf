package com.luo.miaosha.redis;

public abstract class BaseKeyPrefix implements KeyPrefix {
    private int expireSeconds;

    private String prefix;

    public BaseKeyPrefix() {
    }
    public BaseKeyPrefix( String prefix) {
        this(0, prefix);
    }
    public BaseKeyPrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {
        return 0;//永不过期
    }

    @Override
    public String getPrefix() {
        String simpleName = getClass().getSimpleName();
        return simpleName+":"+prefix;
    }
}
