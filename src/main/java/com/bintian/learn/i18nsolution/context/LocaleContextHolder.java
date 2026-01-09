package com.bintian.learn.i18nsolution.context;

import java.util.Locale;

/**
 * 语言上下文持有者
 * 使用ThreadLocal存储当前请求的语言偏好
 */
public class LocaleContextHolder {

    private static final ThreadLocal<Locale> LOCALE_HOLDER = new ThreadLocal<>();

    private static final Locale DEFAULT_LOCALE = Locale.SIMPLIFIED_CHINESE;

    public static void setLocale(Locale locale) {
        LOCALE_HOLDER.set(locale);
    }

    public static Locale getLocale() {
        Locale locale = LOCALE_HOLDER.get();
        return locale != null ? locale : DEFAULT_LOCALE;
    }

    public static String getLocaleString() {
        return getLocale().toLanguageTag();
    }

    public static void clear() {
        LOCALE_HOLDER.remove();
    }
}
