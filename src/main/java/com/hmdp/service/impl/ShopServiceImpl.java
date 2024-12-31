package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.BloomFilterService;
import com.hmdp.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Objects;

import static com.hmdp.utils.RedisConstants.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
@Slf4j
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    private BloomFilterService bloomFilterService;
    @Override
    public Result queryById(Long id) {
        String key = CACHE_SHOP_KEY+id;
        String shopJSON = stringRedisTemplate.opsForValue().get(key);
        if(!bloomFilterService.mightContain(key)){
            log.info("布隆过滤器验证{}在redis里不存在！",key);
            return Result.fail("查询不存在的值！");
        }
        if(Objects.nonNull(shopJSON)){
            Shop bean = JSONUtil.toBean(shopJSON, Shop.class);
            return Result.ok(bean);
        }
        Shop shop = getById(id);
        //todo 在这里是不是可以加一个布隆过滤器（或者拦截器里统一加一个布隆过滤器，拦截所有query的操作 泛型）
        if(shop==null){
            return Result.fail("店铺不存在");
        }
        stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(shop));
        return Result.ok(shop);
    }

    @Override
    public Result update(Shop shop) {
        Long id = shop.getId();
        if (id == null) {
            return Result.fail("店铺id不能为空");
        }
        updateById(shop);
        String key = CACHE_SHOP_KEY+id;
        stringRedisTemplate.delete(key);
        return Result.ok();

    }
}
