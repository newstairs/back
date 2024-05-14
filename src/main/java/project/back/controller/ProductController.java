package project.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.back.dto.ProductDto;
import project.back.entity.Product;
import project.back.repository.ProductRepository;
import project.back.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    /** 상품 리스트 가져오기 */
    @GetMapping
    public List<ProductDto> findAllProductDtos() {
        return productRepository.findAll().stream()
                .map(this::convertToProductDto)
                .collect(Collectors.toList());
    }

    /** 이미지를 Base64로 인코딩하고, ProductDto로 반환 */
    private ProductDto convertToProductDto(Product product) {
        String base64Image =
                "data:image/jpeg;base64," + productService.encodeImageToBase64(product.getProductImgUrl());
        return new ProductDto(product.getProductName(), base64Image);
    }
}