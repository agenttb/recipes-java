package com.bintian.learn.i18nsolution.example.controller;

import com.bintian.learn.i18nsolution.annotation.I18n;
import com.bintian.learn.i18nsolution.example.dto.ProductDTO;
import com.bintian.learn.i18nsolution.example.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品Controller示例
 * 演示如何使用@I18n注解启用国际化
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 获取单个产品（启用国际化）
     */
    @GetMapping("/{id}")
    @I18n
    public ProductDTO getProduct(@PathVariable("id") Long id) {
        return productService.findById(id);
    }

    /**
     * 获取产品列表（启用国际化，指定分类）
     */
    @GetMapping
    @I18n(category = "product")
    public List<ProductDTO> listProducts() {
        return productService.findAll();
    }

    /**
     * 获取产品（不启用国际化）
     */
    @GetMapping("/raw/{id}")
    public ProductDTO getProductRaw(@PathVariable("id") Long id) {
        return productService.findById(id);
    }
}
