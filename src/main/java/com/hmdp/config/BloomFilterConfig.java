package com.hmdp.config;

import com.hmdp.utils.RBloomFilterFactoryBean;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BloomFilterConfig {

    @Bean
    public RBloomFilterFactoryBean<String> rBloomFilterFactoryBean(RedissonClient redissonClient) {
        RBloomFilterFactoryBean<String> factoryBean = new RBloomFilterFactoryBean<>(redissonClient);
        factoryBean.setName("myBloomFilter");
        factoryBean.setExpectedInsertions(10000L); // 预计插入数量
        factoryBean.setFalseProbability(0.01); // 误判率
        return factoryBean;
    }

    @Bean
    public RBloomFilter<String> rBloomFilter(RBloomFilterFactoryBean<String> factoryBean) throws Exception {
        return factoryBean.getObject();
    }
}
