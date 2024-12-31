package com.hmdp.utils.annotation;

import org.springframework.boot.actuate.endpoint.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: redis查询前的bloomcheck
 * @author: dieselengine
 * @create: 2024-12-31 17:06
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface redisPreCheck {
    //数据库操作类型：UPDATE INSERT
    OperationType value();
}
