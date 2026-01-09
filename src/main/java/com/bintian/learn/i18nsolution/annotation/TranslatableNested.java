package com.bintian.learn.i18nsolution.annotation;

import java.lang.annotation.*;

/**
 * 字段级注解，标注字段是嵌套对象或集合，需要递归处理
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TranslatableNested {
}
