package com.bintian.learn.i18nsolution.aspect;

import com.bintian.learn.i18nsolution.annotation.I18n;
import com.bintian.learn.i18nsolution.annotation.Translatable;
import com.bintian.learn.i18nsolution.annotation.TranslatableNested;
import com.bintian.learn.i18nsolution.context.LocaleContextHolder;
import com.bintian.learn.i18nsolution.service.TranslationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 国际化翻译AOP切面
 * 拦截@I18n注解的方法，递归翻译返回值中@Translatable标注的字段
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class I18nTranslationAspect {

    private final TranslationService translationService;

    private static final int MAX_DEPTH = 10;

    private static final ThreadLocal<Set<Integer>> PROCESSED_OBJECTS =
            ThreadLocal.withInitial(HashSet::new);

    @Pointcut("@annotation(com.bintian.learn.i18nsolution.annotation.I18n)")
    public void i18nPointcut() {
    }

    @Around("i18nPointcut()")
    public Object translateResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        if (result == null) {
            return null;
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        I18n i18n = signature.getMethod().getAnnotation(I18n.class);

        if (i18n == null || !i18n.enabled()) {
            return result;
        }

        try {
            PROCESSED_OBJECTS.get().clear();

            Locale locale = LocaleContextHolder.getLocale();
            String category = i18n.category();
            I18n.FallbackStrategy fallback = i18n.fallback();

            // 第一步：收集所有需要翻译的key
            Set<String> keysToTranslate = new LinkedHashSet<>();
            collectTranslationKeys(result, keysToTranslate, category, 0);

            if (keysToTranslate.isEmpty()) {
                return result;
            }

            // 第二步：批量获取翻译
            Map<String, String> translations = translationService
                    .translateBatch(new ArrayList<>(keysToTranslate), locale);

            // 第三步：应用翻译
            PROCESSED_OBJECTS.get().clear();
            applyTranslations(result, translations, category, fallback, 0);

            return result;
        } finally {
            PROCESSED_OBJECTS.get().clear();
        }
    }

    /**
     * 收集所有需要翻译的key
     */
    private void collectTranslationKeys(Object obj, Set<String> keys,
                                        String defaultCategory, int depth) {
        if (obj == null || depth > MAX_DEPTH) {
            return;
        }

        int objectId = System.identityHashCode(obj);
        if (PROCESSED_OBJECTS.get().contains(objectId)) {
            return;
        }
        PROCESSED_OBJECTS.get().add(objectId);

        Class<?> clazz = obj.getClass();

        // 处理集合类型
        if (obj instanceof Collection<?> collection) {
            for (Object element : collection) {
                collectTranslationKeys(element, keys, defaultCategory, depth + 1);
            }
            return;
        }

        // 处理数组类型
        if (clazz.isArray()) {
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                Object element = Array.get(obj, i);
                collectTranslationKeys(element, keys, defaultCategory, depth + 1);
            }
            return;
        }

        // 处理Map类型
        if (obj instanceof Map<?, ?> map) {
            for (Object value : map.values()) {
                collectTranslationKeys(value, keys, defaultCategory, depth + 1);
            }
            return;
        }

        // 跳过基本类型和包装类
        if (isSimpleType(clazz)) {
            return;
        }

        // 处理对象字段
        for (Field field : getAllFields(clazz)) {
            field.setAccessible(true);

            try {
                Object fieldValue = field.get(obj);
                if (fieldValue == null) {
                    continue;
                }

                // 处理@Translatable注解的字段
                if (field.isAnnotationPresent(Translatable.class)) {
                    Translatable translatable = field.getAnnotation(Translatable.class);
                    String key = buildTranslationKey(obj, field, fieldValue, translatable);
                    if (StringUtils.hasText(key)) {
                        keys.add(key);
                    }
                }

                // 处理@TranslatableNested注解的字段（递归）
                if (field.isAnnotationPresent(TranslatableNested.class)) {
                    collectTranslationKeys(fieldValue, keys, defaultCategory, depth + 1);
                }

            } catch (IllegalAccessException e) {
                log.warn("Cannot access field: {}", field.getName(), e);
            }
        }
    }

    /**
     * 应用翻译到对象
     */
    private void applyTranslations(Object obj, Map<String, String> translations,
                                   String defaultCategory,
                                   I18n.FallbackStrategy fallback, int depth) {
        if (obj == null || depth > MAX_DEPTH) {
            return;
        }

        int objectId = System.identityHashCode(obj);
        if (PROCESSED_OBJECTS.get().contains(objectId)) {
            return;
        }
        PROCESSED_OBJECTS.get().add(objectId);

        Class<?> clazz = obj.getClass();

        // 处理集合
        if (obj instanceof Collection<?> collection) {
            for (Object element : collection) {
                applyTranslations(element, translations, defaultCategory, fallback, depth + 1);
            }
            return;
        }

        // 处理数组
        if (clazz.isArray()) {
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                Object element = Array.get(obj, i);
                applyTranslations(element, translations, defaultCategory, fallback, depth + 1);
            }
            return;
        }

        // 处理Map
        if (obj instanceof Map<?, ?> map) {
            for (Object value : map.values()) {
                applyTranslations(value, translations, defaultCategory, fallback, depth + 1);
            }
            return;
        }

        // 跳过基本类型
        if (isSimpleType(clazz)) {
            return;
        }

        // 处理对象字段
        for (Field field : getAllFields(clazz)) {
            field.setAccessible(true);

            try {
                Object fieldValue = field.get(obj);
                if (fieldValue == null) {
                    continue;
                }

                // 应用翻译到@Translatable字段
                if (field.isAnnotationPresent(Translatable.class)) {
                    Translatable translatable = field.getAnnotation(Translatable.class);
                    String key = buildTranslationKey(obj, field, fieldValue, translatable);

                    if (StringUtils.hasText(key) && translations.containsKey(key)) {
                        String translatedValue = translations.get(key);
                        String finalValue = determineFinalValue(
                                key, translatedValue, fieldValue.toString(), fallback);
                        field.set(obj, finalValue);
                    }
                }

                // 递归处理嵌套对象
                if (field.isAnnotationPresent(TranslatableNested.class)) {
                    applyTranslations(fieldValue, translations, defaultCategory, fallback, depth + 1);
                }

            } catch (IllegalAccessException e) {
                log.warn("Cannot set field: {}", field.getName(), e);
            }
        }
    }

    /**
     * 构建翻译key
     */
    private String buildTranslationKey(Object obj, Field field, Object fieldValue,
                                       Translatable translatable) {
        String keyPrefix = translatable.keyPrefix();
        String keyField = translatable.keyField();

        String baseKey;

        // 如果指定了keyField，则从该字段获取key
        if (StringUtils.hasText(keyField)) {
            try {
                Field sourceField = findField(obj.getClass(), keyField);
                if (sourceField != null) {
                    sourceField.setAccessible(true);
                    Object sourceValue = sourceField.get(obj);
                    baseKey = sourceValue != null ? sourceValue.toString() : "";
                } else {
                    baseKey = fieldValue.toString();
                }
            } catch (Exception e) {
                log.warn("Cannot get keyField: {}", keyField, e);
                baseKey = fieldValue.toString();
            }
        } else {
            baseKey = fieldValue.toString();
        }

        // 拼接前缀
        if (StringUtils.hasText(keyPrefix)) {
            return keyPrefix + baseKey;
        }

        return baseKey;
    }

    /**
     * 根据策略决定最终值
     */
    private String determineFinalValue(String key, String translatedValue,
                                       String originalValue,
                                       I18n.FallbackStrategy fallback) {
        // 如果翻译值和key相同，说明没找到翻译
        if (key.equals(translatedValue)) {
            return switch (fallback) {
                case KEEP_ORIGINAL -> originalValue;
                case RETURN_KEY -> key;
                case RETURN_EMPTY -> "";
            };
        }
        return translatedValue;
    }

    /**
     * 获取类的所有字段（包括父类）
     */
    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    /**
     * 查找字段（包括父类）
     */
    private Field findField(Class<?> clazz, String fieldName) {
        while (clazz != null && clazz != Object.class) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 判断是否为简单类型
     */
    private boolean isSimpleType(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz == String.class ||
                Number.class.isAssignableFrom(clazz) ||
                clazz == Boolean.class ||
                clazz == Character.class ||
                clazz.isEnum() ||
                clazz == java.util.Date.class ||
                clazz == java.time.LocalDate.class ||
                clazz == java.time.LocalDateTime.class ||
                clazz == java.time.LocalTime.class ||
                clazz == java.time.Instant.class;
    }
}
