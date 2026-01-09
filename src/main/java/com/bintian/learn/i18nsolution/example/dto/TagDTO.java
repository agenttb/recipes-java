package com.bintian.learn.i18nsolution.example.dto;

import com.bintian.learn.i18nsolution.annotation.Translatable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签DTO示例
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {

    private Long id;

    private String code;

    @Translatable(keyPrefix = "tag.")
    private String label;
}
