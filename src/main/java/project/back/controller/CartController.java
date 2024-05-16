package project.back.controller;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.back.dto.ApiResponse;
import project.back.dto.CartProductDto;
import project.back.dto.ProductDto;
import project.back.dto.ProductSearchDto;
import project.back.etc.RequestMemberMapper;
import project.back.etc.aboutlogin.JwtUtill;
import project.back.service.CartService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final RequestMemberMapper requestMemberMapper;

    /**
     * productName을 포함하는 검색한 모든 재료 검색
     * @param productName 상품이름(String)
     * @return 상품이름을 포함하는 모든 재료
     */
    @GetMapping("/{productName}")
    public ResponseEntity<ApiResponse<List<ProductSearchDto>>> findAllByProductName(@PathVariable String productName){

        ApiResponse<List<ProductSearchDto>> result = cartService.findAllByProductName(productName);

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CartProductDto>> addProduct(
            @RequestBody CartProductDto cartProductDto, HttpServletRequest request
            ){
        Long memberId = requestMemberMapper.RequestToMemberId(request); // TODO: JwtUtil자체에서 해결하도록 해보자
        ApiResponse<CartProductDto> result = cartService.addProduct(cartProductDto, memberId);
        return ResponseEntity.ok(result);
    }

}
