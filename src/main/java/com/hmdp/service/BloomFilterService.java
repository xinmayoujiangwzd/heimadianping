package com.hmdp.service;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Closeable;
import java.util.HashSet;
import java.util.Set;

@Service
public class BloomFilterService {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private final RBloomFilter<String> bloomFilter;

    @PostConstruct // 项目启动的时候执行该方法，也可以理解为在spring容器初始化的时候执行该方法
    public void init() {
        // 启动项目时初始化bloomFilter
        Set<String> keys = showAllKeys();
        for (String key:keys) {
            bloomFilter.add(key);
        }
    }

    private Set<String> showAllKeys(){
        Set<String> res = new HashSet<>();
        // 使用 SCAN 命令扫描 Redis 中的键
        Cursor<byte[]> cursor = redisTemplate.executeWithStickyConnection(
                connection -> connection.scan(ScanOptions.scanOptions().match("*").count(100).build())
        );

        while (cursor.hasNext()) {
            byte[] next = cursor.next();
            String key = new String(next);
            res.add(key);
        }
        return res;
    }

    public BloomFilterService(RBloomFilter<String> bloomFilter) {
        this.bloomFilter = bloomFilter;
    }

    public void addToBloomFilter(String value) {
        bloomFilter.add(value);
    }

    public boolean mightContain(String value) {
        return bloomFilter.contains(value);
    }
}
