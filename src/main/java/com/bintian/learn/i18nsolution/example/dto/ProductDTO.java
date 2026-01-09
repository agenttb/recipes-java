package com.bintian.learn.i18nsolution.example.dto;

import com.bintian.learn.i18nsolution.annotation.Translatable;
import com.bintian.learn.i18nsolution.annotation.TranslatableNested;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 产品DTO示例
 * 演示如何使用@Translatable和@TranslatableNested注解
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;

    private String code;

    @Translatable(keyPrefix = "product.name.")
    private String name;

    @Translatable(keyPrefix = "product.desc.", keyField = "code")
    private String description;

    @Translatable(keyPrefix = "status.")
    private String status;

    private BigDecimal price;

    @TranslatableNested
    private CategoryDTO category;

    @TranslatableNested
    private List<TagDTO> tags;
}
