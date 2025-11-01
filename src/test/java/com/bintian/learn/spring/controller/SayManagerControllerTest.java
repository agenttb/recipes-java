package com.bintian.learn.spring.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// 2. 导入所有结果匹配器 (如: status(), content(), jsonPath())
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = SayManagerController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration
public class SayManagerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Configuration
    @Import(SayManagerController.class) // <-- 只加载这个 Controller
    static class TestConfig {
        // 这个类是空的，它只需要 @Import 注解
        // Spring 会在这里创建一个 Spring 上下文
    }

    @Test
    void testSayHello() throws Exception {
        mockMvc.perform(get("/manage/initData")) // 应该用 post()
                .andExpect(status().isOk());
    }
}
