package project.back.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.back.dto.ApiResponse;
import project.back.dto.CartDto;
import project.back.dto.ProductSearchDto;
import project.back.etc.RequestMemberMapper;
import project.back.service.CartService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final RequestMemberMapper requestMemberMapper;

    /**
     * [GET] 장바구니 목록 조회
     *
     * @param request 유저정보를 포함하고있는 HttpServletRequest
     * @return 장바구니 목록
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CartDto>>> getCart(HttpServletRequest request) {
        Long memberId = requestMemberMapper.RequestToMemberId(request);

        ApiResponse<List<CartDto>> result = cartService.getCartsByMemberId(memberId);
        return ResponseEntity.ok(result);
    }

    /**
     * [GET] productName 을 포함하는 검색한 모든 재료 검색
     *
     * @param productName 상품이름(String)
     * @return 상품이름을 포함하는 모든 재료
     */
    @GetMapping("/{productName}")
    public ResponseEntity<ApiResponse<List<ProductSearchDto>>> findAllByProductName(@PathVariable String productName) {

        ApiResponse<List<ProductSearchDto>> result = cartService.findAllByProductName(productName);
        return ResponseEntity.ok(result);
    }

    /**
     * [POST] 장바구니에 상품추가
     *
     * @param cartDto (productId)
     * @param request 유저정보를 포함하고있는 HttpServletRequest
     * @return 저장된 상품에 대한 객체(productId, quantity)와 상품이름
     */
    @PostMapping
    public ResponseEntity<ApiResponse<List<CartDto>>> addProduct(
            @RequestBody CartDto cartDto, HttpServletRequest request) {
        // TODO: JwtUtil 자체에서 해결하도록 해보자
        Long memberId = requestMemberMapper.RequestToMemberId(request);
        ApiResponse<List<CartDto>> result = cartService.addProduct(cartDto, memberId);
        return ResponseEntity.ok(result);
    }

    /**
     * [PATCH] 장바구니의 특정 상품 수량 변경
     *
     * @param productId 상품 고유번호
     * @param sign      "+", "-", "{수량}"
     * @param request   유저정보를 포함하고있는 HttpServletRequest
     * @return 장바구니 목록
     */
    @PatchMapping("/{productId}/{sign}")
    public ResponseEntity<ApiResponse<List<CartDto>>> updateQuantity(
            @PathVariable Long productId, @PathVariable String sign, HttpServletRequest request) {
        Long memberId = requestMemberMapper.RequestToMemberId(request);
        ApiResponse<List<CartDto>> result = cartService.updateQuantity(productId, sign, memberId);
        return ResponseEntity.ok(result);
    }

    /**
     * [DELETE] 장바구니 상품 삭제 (개별)
     *
     * @param productId 상품 고유번호
     * @param request 유저정보를 포함하고있는 HttpServletRequest
     * @return 장바구니 목록
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<List<CartDto>>> deleteProduct(@PathVariable Long productId,
                                                                    HttpServletRequest request) {
        Long memberId = requestMemberMapper.RequestToMemberId(request);
        ApiResponse<List<CartDto>> result = cartService.deleteProduct(productId, memberId);

        return ResponseEntity.ok(result);
    }

    /**
     * [DELETE] 장바구니 상품 삭제 (전체)
     *
     * @param request 유저정보를 포함하고있는 HttpServletRequest
     * @return 빈 장바구니 목록
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<List<CartDto>>> deleteAllProduct(HttpServletRequest request){
        Long memberId = requestMemberMapper.RequestToMemberId(request);
        ApiResponse<List<CartDto>> result = cartService.deleteAllProduct(memberId);

        return ResponseEntity.ok(result);
    }
}
