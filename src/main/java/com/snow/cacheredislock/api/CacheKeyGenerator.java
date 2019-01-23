package com.snow.cacheredislock.api;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * key生成器
 *
 * @author 王鹏涛
 * @since 2019年1月23日
 */
public interface CacheKeyGenerator {

    /**
     * 获取AOP参数,生成指定缓存Key
     *
     * @param pjp PJP
     * @return 缓存KEY
     */
    String getLockKey(ProceedingJoinPoint pjp);
}