package com.bintian.learn.i18nsolution.annotation;

import java.lang.annotation.*;

/**
 * 字段级注解，标注字段需要翻译
 * 仅支持String类型字段
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Translatable {

    /**
     * 翻译key的前缀，与字段值拼接形成完整key
     * 例如: prefix="product.name." + fieldValue="apple" => "product.name.apple"
     */
    String keyPrefix() default "";

    /**
     * 指定使用另一个字段的值作为翻译key
     * 为空则使用当前字段的值
     */
    String keyField() default "";

    /**
     * 翻译分类
     */
    String category() default "";
}
