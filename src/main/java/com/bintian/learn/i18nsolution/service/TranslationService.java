package com.bintian.learn.i18nsolution.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 翻译服务接口
 */
public interface TranslationService {

    /**
     * 获取单个翻译
     */
    String translate(String key, Locale locale);

    /**
     * 获取单个翻译，带默认值
     */
    String translate(String key, Locale locale, String defaultValue);

    /**
     * 批量翻译（性能优化）
     */
    Map<String, String> translateBatch(List<String> keys, Locale locale);

    /**
     * 按分类获取所有翻译
     */
    Map<String, String> getTranslationsByCategory(String category, Locale locale);

    /**
     * 刷新缓存
     */
    void refreshCache();

    /**
     * 刷新指定locale的缓存
     */
    void refreshCache(Locale locale);
}
