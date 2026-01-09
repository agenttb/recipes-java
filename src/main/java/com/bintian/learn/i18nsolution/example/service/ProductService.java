package com.bintian.learn.i18nsolution.example.service;

import com.bintian.learn.i18nsolution.example.dto.CategoryDTO;
import com.bintian.learn.i18nsolution.example.dto.ProductDTO;
import com.bintian.learn.i18nsolution.example.dto.TagDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 产品服务示例（模拟数据）
 */
@Service
public class ProductService {

    public ProductDTO findById(Long id) {
        return createMockProduct(id);
    }

    public List<ProductDTO> findAll() {
        return Arrays.asList(
                createMockProduct(1L),
                createMockProduct(2L)
        );
    }

    private ProductDTO createMockProduct(Long id) {
        String productCode = id == 1L ? "apple" : "banana";

        return ProductDTO.builder()
                .id(id)
                .code(productCode)
                .name(productCode)  // 翻译key: product.name.apple
                .description("desc")  // 翻译key: product.desc.apple (使用code字段)
                .status("active")  // 翻译key: status.active
                .price(new BigDecimal("9.99"))
                .category(CategoryDTO.builder()
                        .id(100L)
                        .code("electronics")
                        .name("electronics")  // 翻译key: category.electronics
                        .build())
                .tags(Arrays.asList(
                        TagDTO.builder()
                                .id(1L)
                                .code("hot")
                                .label("hot")  // 翻译key: tag.hot
                                .build(),
                        TagDTO.builder()
                                .id(2L)
                                .code("new")
                                .label("new")  // 翻译key: tag.new
                                .build()
                ))
                .build();
    }
}
