package com.bintian.learn.i18nsolution.service.impl;

import com.bintian.learn.i18nsolution.entity.Translation;
import com.bintian.learn.i18nsolution.repository.TranslationRepository;
import com.bintian.learn.i18nsolution.service.TranslationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslationServiceImpl implements TranslationService {

    private final TranslationRepository translationRepository;

    private static final String CACHE_NAME = "translations";

    @Override
    @Cacheable(value = CACHE_NAME, key = "#key + '_' + #locale.toLanguageTag()")
    public String translate(String key, Locale locale) {
        return translate(key, locale, key);
    }

    @Override
    public String translate(String key, Locale locale, String defaultValue) {
        return translationRepository
                .findByMessageKeyAndLocale(key, locale.toLanguageTag())
                .map(Translation::getMessageValue)
                .orElse(defaultValue);
    }

    @Override
    public Map<String, String> translateBatch(List<String> keys, Locale locale) {
        if (keys == null || keys.isEmpty()) {
            return Collections.emptyMap();
        }

        String localeStr = locale.toLanguageTag();
        List<Translation> translations = translationRepository
                .findByMessageKeysAndLocale(keys, localeStr);

        // 转换为Map
        Map<String, String> result = translations.stream()
                .collect(Collectors.toMap(
                        Translation::getMessageKey,
                        Translation::getMessageValue,
                        (v1, v2) -> v1
                ));

        // 对于没有找到翻译的key，保留原值
        for (String key : keys) {
            result.putIfAbsent(key, key);
        }

        return result;
    }

    @Override
    public Map<String, String> getTranslationsByCategory(String category, Locale locale) {
        List<Translation> translations = translationRepository
                .findByCategoryAndLocale(category, locale.toLanguageTag());

        return translations.stream()
                .collect(Collectors.toMap(
                        Translation::getMessageKey,
                        Translation::getMessageValue
                ));
    }

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void refreshCache() {
        log.info("Translation cache refreshed");
    }

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void refreshCache(Locale locale) {
        log.info("Translation cache refreshed for locale: {}", locale.toLanguageTag());
    }
}
