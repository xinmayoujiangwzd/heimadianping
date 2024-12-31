package com.hmdp.utils;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description: 布隆过滤器工具类
 * @author: dieselengine
 * @create: 2024-12-30 17:10
 **/
@Component
public class BloomFilterUtil {
    @Resource
    private RedissonClient redissonClient;

    public<T> RBloomFilter<T> create(String filtername,long expectedInsertions,double falsePositiveRate) {
        RBloomFilter<T> bloomFilter = redissonClient.getBloomFilter(filtername);
        bloomFilter.tryInit(expectedInsertions, falsePositiveRate);
        return bloomFilter;
    }


}
