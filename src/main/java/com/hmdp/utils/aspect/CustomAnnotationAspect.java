package com.hmdp.utils.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;
@Aspect
@Component
@Slf4j
public class CustomAnnotationAspect {

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.hmdp.utils.annotation.redisPreCheck)")
    public void autoFillPointCut(){}

    /**
     * 前置通知，在通知中进行公共字段的赋值
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("进行布隆过滤器查询...");
        Object[] args = joinPoint.getArgs();


    }
}
