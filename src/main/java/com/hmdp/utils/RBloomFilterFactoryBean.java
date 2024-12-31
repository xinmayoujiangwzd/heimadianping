package com.hmdp.utils;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.FactoryBean;

public class RBloomFilterFactoryBean<T> implements FactoryBean<RBloomFilter<T>> {

    private final RedissonClient redissonClient;
    private String name; // 布隆过滤器名称
    private long expectedInsertions; // 预计插入数量
    private double falseProbability; // 误判率

    public RBloomFilterFactoryBean(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    // 设置布隆过滤器的参数
    public void setName(String name) {
        this.name = name;
    }

    public void setExpectedInsertions(long expectedInsertions) {
        this.expectedInsertions = expectedInsertions;
    }

    public void setFalseProbability(double falseProbability) {
        this.falseProbability = falseProbability;
    }

    @Override
    public RBloomFilter<T> getObject() {
        RBloomFilter<T> bloomFilter = redissonClient.getBloomFilter(name);
        bloomFilter.tryInit(expectedInsertions, falseProbability);
        return bloomFilter;
    }

    @Override
    public Class<?> getObjectType() {
        return RBloomFilter.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
