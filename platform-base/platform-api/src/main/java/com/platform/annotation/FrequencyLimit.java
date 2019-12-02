package com.platform.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 频繁访问限制
 */
/**
 * @Retention：注解的保留位置
 * @Retention(RetentionPolicy.SOURCE) //注解仅存在于源码中，在class字节码文件中不包含
 * @Retention(RetentionPolicy.CLASS) // 默认的保留策略，注解会在class字节码文件中存在，但运行时无法获得，
 * @Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
 */
@Retention(RetentionPolicy.RUNTIME)
/**
 * @Target:注解的作用目标
 * @Target(ElementType.TYPE)   //接口、类、枚举、注解
 * @Target(ElementType.FIELD) //字段、枚举的常量
 * @Target(ElementType.METHOD) //方法
 * @Target(ElementType.PARAMETER) //方法参数
 * @Target(ElementType.CONSTRUCTOR)  //构造函数
 * @Target(ElementType.LOCAL_VARIABLE)  //局部变量
 * @Target(ElementType.ANNOTATION_TYPE)  //注解
 * @Target(ElementType.PACKAGE) ///包
 */
@Target(ElementType.METHOD)
@Documented
public @interface FrequencyLimit {
	//访问请求动作
    String action() default "action";
    //单位时间允许访问次数 - - -默认值是1
    int count() default 1;
    //设置单位时间为1分钟 - - - 默认值是1分钟
    int time() default 60;
    //限制单位时间为1分钟 - - - 默认值是1分钟
    int forbiddenTime() default 60;
    
    
}
