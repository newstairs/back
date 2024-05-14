package project.back.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.back.dto.ApiResponse;
import project.back.dto.ProductDto;
import project.back.entity.Product;
import project.back.service.CartService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    /**
     * productName을 포함하는 검색한 모든 재료 검색
     * @param productName 상품이름(String)
     * @return 상품이름을 포함하는 모든 재료
     */
    @GetMapping("/{productName}")
    public ResponseEntity<ApiResponse<List<ProductDto>>> findAllByItem(@PathVariable String productName){

        ApiResponse<List<ProductDto>> result = cartService.findAllByProductName(productName);

        return ResponseEntity.ok(result);
    }

}
