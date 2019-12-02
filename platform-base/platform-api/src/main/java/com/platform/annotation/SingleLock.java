package com.platform.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 单例锁--跟用户钱包相关使用
 */
@Retention(RetentionPolicy.RUNTIME)

@Target(ElementType.METHOD)
@Documented
public @interface SingleLock {
}
