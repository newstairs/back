package project.back.controller;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.back.dto.ApiResponse;
import project.back.dto.CartDto;
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
     * [GET] productName을 포함하는 검색한 모든 재료 검색
     * @param productName 상품이름(String)
     * @return 상품이름을 포함하는 모든 재료
     */
    @GetMapping("/{productName}")
    public ResponseEntity<ApiResponse<List<ProductSearchDto>>> findAllByProductName(@PathVariable String productName){

        ApiResponse<List<ProductSearchDto>> result = cartService.findAllByProductName(productName);

        return ResponseEntity.ok(result);
    }

    /**
     * [POST] 장바구니에 상품추가
     * @param cartProductDto (productId)
     * @param request 유저정보를 포함하고있는 HttpServletRequest
     * @return 저장된 상품에 대한 객체(productId, quantity)와 상품이름
     */
    @PostMapping
    public ResponseEntity<ApiResponse<List<CartDto>>> addProduct(
            @RequestBody CartDto cartDto, HttpServletRequest request ){
        // TODO: JwtUtil자체에서 해결하도록 해보자
        Long memberId = requestMemberMapper.RequestToMemberId(request);
        ApiResponse<List<CartDto>> result = cartService.addProduct(cartDto, memberId);
        return ResponseEntity.ok(result);
    }

    /**
     * [PATCH] 장바구니의 특정 상품 수량 변경
     * @param productId
     * @param sign "+", "-", "{수량}"
     * @param request 유저정보를 포함하고있는 HttpServletRequest
     * @return 장바구니 리스트
     */
    @PatchMapping("/{productId}/{sign}")
    public ResponseEntity<ApiResponse<List<CartDto>>> updateQuantity(
            @PathVariable Long productId, @PathVariable String sign, HttpServletRequest request){
        Long memberId = requestMemberMapper.RequestToMemberId(request);
        ApiResponse<List<CartDto>> result = cartService.updateQuantity(productId, sign, memberId);
        return ResponseEntity.ok(result);
    }

}
