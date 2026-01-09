package com.bintian.learn.i18nsolution.benchmark;

import com.bintian.learn.i18nsolution.annotation.Translatable;
import com.bintian.learn.i18nsolution.annotation.TranslatableNested;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 反射 vs Jackson JSON Tree 性能对比测试
 */
public class TranslationBenchmarkTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int WARMUP_ITERATIONS = 1000;
    private static final int TEST_ITERATIONS = 10000;

    // 模拟翻译字典
    private static final Map<String, String> TRANSLATIONS = Map.of(
            "product.name.apple", "苹果",
            "product.desc.apple", "新鲜苹果",
            "status.active", "激活",
            "category.electronics", "电子产品",
            "tag.hot", "热门",
            "tag.new", "新品"
    );

    @Test
    void benchmarkComparison() {
        System.out.println("=== 翻译方案性能对比测试 ===\n");

        // 预热
        System.out.println("预热中...");
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            TestProductDTO product = createTestProduct();
            translateByReflection(product);

            product = createTestProduct();
            translateByJackson(product);
        }

        // 测试反射方式
        System.out.println("\n测试反射方式...");
        long reflectionStart = System.nanoTime();
        for (int i = 0; i < TEST_ITERATIONS; i++) {
            TestProductDTO product = createTestProduct();
            translateByReflection(product);
        }
        long reflectionTime = System.nanoTime() - reflectionStart;

        // 测试Jackson方式
        System.out.println("测试Jackson JSON Tree方式...");
        long jacksonStart = System.nanoTime();
        for (int i = 0; i < TEST_ITERATIONS; i++) {
            TestProductDTO product = createTestProduct();
            translateByJackson(product);
        }
        long jacksonTime = System.nanoTime() - jacksonStart;

        // 输出结果
        System.out.println("\n=== 测试结果 ===");
        System.out.println("迭代次数: " + TEST_ITERATIONS);
        System.out.printf("反射方式总耗时: %.2f ms (平均: %.4f ms/次)%n",
                reflectionTime / 1_000_000.0, reflectionTime / 1_000_000.0 / TEST_ITERATIONS);
        System.out.printf("Jackson方式总耗时: %.2f ms (平均: %.4f ms/次)%n",
                jacksonTime / 1_000_000.0, jacksonTime / 1_000_000.0 / TEST_ITERATIONS);
        System.out.printf("性能比: Jackson/反射 = %.2fx%n", (double) jacksonTime / reflectionTime);

        if (reflectionTime < jacksonTime) {
            System.out.println("\n结论: 反射方式更快");
        } else {
            System.out.println("\n结论: Jackson方式更快");
        }
    }

    /**
     * 方案1: 反射方式翻译
     */
    private void translateByReflection(Object obj) {
        if (obj == null) return;

        Class<?> clazz = obj.getClass();

        // 处理集合
        if (obj instanceof Collection<?> collection) {
            for (Object element : collection) {
                translateByReflection(element);
            }
            return;
        }

        // 处理普通对象
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                if (value == null) continue;

                // 处理@Translatable字段
                if (field.isAnnotationPresent(Translatable.class)) {
                    Translatable t = field.getAnnotation(Translatable.class);
                    String key = t.keyPrefix() + value.toString();
                    String translated = TRANSLATIONS.getOrDefault(key, value.toString());
                    field.set(obj, translated);
                }

                // 递归处理嵌套对象
                if (field.isAnnotationPresent(TranslatableNested.class)) {
                    translateByReflection(value);
                }
            } catch (IllegalAccessException e) {
                // ignore
            }
        }
    }

    /**
     * 方案2: Jackson JSON Tree方式翻译
     * 注意：此方案需要额外配置来标识哪些字段需要翻译
     */
    private TestProductDTO translateByJackson(TestProductDTO product) {
        try {
            // 1. 转换为JsonNode
            JsonNode rootNode = objectMapper.valueToTree(product);

            // 2. 遍历并翻译（需要硬编码或配置哪些路径需要翻译）
            translateJsonNode(rootNode, "");

            // 3. 转换回对象
            return objectMapper.treeToValue(rootNode, TestProductDTO.class);
        } catch (Exception e) {
            return product;
        }
    }

    // 需要硬编码翻译路径（因为JSON丢失了注解信息）
    private static final Set<String> TRANSLATABLE_PATHS = Set.of(
            "/name", "/description", "/status",
            "/category/name", "/tags/0/label", "/tags/1/label"
    );

    private static final Map<String, String> PATH_TO_PREFIX = Map.of(
            "/name", "product.name.",
            "/description", "product.desc.",
            "/status", "status.",
            "/category/name", "category.",
            "/tags/0/label", "tag.",
            "/tags/1/label", "tag."
    );

    private void translateJsonNode(JsonNode node, String currentPath) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String fieldPath = currentPath + "/" + entry.getKey();
                JsonNode fieldValue = entry.getValue();

                if (fieldValue.isTextual() && TRANSLATABLE_PATHS.contains(fieldPath)) {
                    String prefix = PATH_TO_PREFIX.get(fieldPath);
                    String key = prefix + fieldValue.asText();
                    String translated = TRANSLATIONS.getOrDefault(key, fieldValue.asText());
                    objectNode.set(entry.getKey(), new TextNode(translated));
                } else if (fieldValue.isObject() || fieldValue.isArray()) {
                    translateJsonNode(fieldValue, fieldPath);
                }
            }
        } else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                translateJsonNode(node.get(i), currentPath + "/" + i);
            }
        }
    }

    private TestProductDTO createTestProduct() {
        return TestProductDTO.builder()
                .id(1L)
                .code("apple")
                .name("apple")
                .description("apple")
                .status("active")
                .category(TestCategoryDTO.builder()
                        .id(100L)
                        .name("electronics")
                        .build())
                .tags(Arrays.asList(
                        TestTagDTO.builder().id(1L).label("hot").build(),
                        TestTagDTO.builder().id(2L).label("new").build()
                ))
                .build();
    }

    // 测试用DTO
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class TestProductDTO {
        private Long id;
        private String code;
        @Translatable(keyPrefix = "product.name.")
        private String name;
        @Translatable(keyPrefix = "product.desc.")
        private String description;
        @Translatable(keyPrefix = "status.")
        private String status;
        @TranslatableNested
        private TestCategoryDTO category;
        @TranslatableNested
        private List<TestTagDTO> tags;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class TestCategoryDTO {
        private Long id;
        @Translatable(keyPrefix = "category.")
        private String name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class TestTagDTO {
        private Long id;
        @Translatable(keyPrefix = "tag.")
        private String label;
    }
}
