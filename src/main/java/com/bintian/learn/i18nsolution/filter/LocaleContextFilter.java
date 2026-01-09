package com.bintian.learn.i18nsolution.filter;

import com.bintian.learn.i18nsolution.context.LocaleContextHolder;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Locale;

/**
 * HTTP过滤器，提取Accept-Language请求头并设置语言上下文
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LocaleContextFilter implements Filter {

    private static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String acceptLanguage = httpRequest.getHeader(ACCEPT_LANGUAGE_HEADER);

            Locale locale = parseLocale(acceptLanguage);
            LocaleContextHolder.setLocale(locale);

            chain.doFilter(request, response);
        } finally {
            LocaleContextHolder.clear();
        }
    }

    private Locale parseLocale(String acceptLanguage) {
        if (!StringUtils.hasText(acceptLanguage)) {
            return Locale.SIMPLIFIED_CHINESE;
        }

        // 解析 Accept-Language header，取第一个首选语言
        // 格式: zh-CN,zh;q=0.9,en;q=0.8
        String primaryLanguage = acceptLanguage.split(",")[0].trim();
        String[] parts = primaryLanguage.split(";")[0].split("-");

        if (parts.length >= 2) {
            return Locale.of(parts[0], parts[1]);
        } else {
            return Locale.of(parts[0]);
        }
    }
}
