package project.back.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.back.dto.ApiResponse;
import project.back.dto.product.ProductDto;
import project.back.service.product.ProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    /**
     * 상품 목록 페이징 처리해서 가져오기
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProductDtos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ApiResponse<List<ProductDto>> productList =
                productService.findAllProductDtos(page, size);
        return ResponseEntity.ok(productList);
    }
}