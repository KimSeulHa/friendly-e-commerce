package com.ecommerce.order.controller;

import com.ecommerce.order.domain.model.ProductDto;
import com.ecommerce.order.service.ProductSearchService;
import com.ecommerce.order.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/search/product")
@RequiredArgsConstructor
public class SearchController {

    private final ProductSearchService searchService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> searchProduct(@RequestParam String search){
        return ResponseEntity.ok(searchService.searchByName(search).stream()
                    .map(product -> ProductDto.withOutItemsFrom(product)).collect(Collectors.toList())
                );
    }
    @GetMapping("/detail")
    public ResponseEntity<ProductDto> getDetail(@RequestParam Long id){
        return ResponseEntity.ok(ProductDto.from(searchService.getItemsByProductId(id)));
    }
}
