package com.bintian.learn.i18nsolution.annotation;

import java.lang.annotation.*;

/**
 * 方法级注解，标注启用国际化翻译
 * AOP切面会拦截此方法的返回值进行翻译处理
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface I18n {

    /**
     * 是否启用翻译，默认true
     */
    boolean enabled() default true;

    /**
     * 翻译分类，用于缩小查询范围
     */
    String category() default "";

    /**
     * 当找不到翻译时的策略
     */
    FallbackStrategy fallback() default FallbackStrategy.KEEP_ORIGINAL;

    enum FallbackStrategy {
        KEEP_ORIGINAL,  // 保持原文
        RETURN_KEY,     // 返回key
        RETURN_EMPTY    // 返回空字符串
    }
}
