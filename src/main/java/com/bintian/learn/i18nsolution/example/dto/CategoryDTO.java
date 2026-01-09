package com.bintian.learn.i18nsolution.example.dto;

import com.bintian.learn.i18nsolution.annotation.Translatable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类DTO示例
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long id;

    private String code;

    @Translatable(keyPrefix = "category.")
    private String name;
}
