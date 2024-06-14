package project.back.controller.cart;

import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.back.configuration.annotation.LoginUser;
import project.back.dto.ApiResponse;
import project.back.dto.cart.CartDto;
import project.back.dto.cart.ProductSearchDto;
import project.back.service.cart.CartService;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    private static final String INVALID_QUANTITY_MESSAGE = "1 이상의 숫자만 입력해주세요";

    /**
     * [GET] 장바구니 목록 조회
     *
     * @param memberId request JWT 토큰 내부의 사용자 Id값
     * @return 장바구니 목록
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CartDto>>> getCart(@LoginUser Long memberId) {
        return ResponseEntity.ok(cartService.getCartsByMemberId(memberId));
    }

    /**
     * [POST] 장바구니에 상품추가
     *
     * @param cartDto  (productId)
     * @param memberId request JWT 토큰 내부의 사용자 Id값
     * @return 저장된 상품에 대한 객체(productId, quantity)와 상품이름
     */
    @PostMapping
    public ResponseEntity<ApiResponse<List<CartDto>>> addProduct(
            @RequestBody CartDto cartDto,
            @LoginUser Long memberId) {

        return ResponseEntity.ok(cartService.addProduct(cartDto, memberId));
    }

    /**
     * [PATCH] 장바구니의 특정 상품 수량 증가(버튼)
     *
     * @param productId 상품 고유번호
     * @param memberId request JWT 토큰 내부의 사용자 Id값
     * @return 장바구니 목록
     */
    @PatchMapping("/plus/{productId}")
    public ResponseEntity<ApiResponse<CartDto>> plusQuantity(
            @PathVariable Long productId,
            @LoginUser Long memberId) {

        return ResponseEntity.ok(cartService.plusQuantity(productId, memberId));
    }

    /**
     * [PATCH] 장바구니의 특정 상품 수량 감소(버튼)
     *
     * @param productId 상품 고유번호
     * @param memberId request JWT 토큰 내부의 사용자 Id값
     * @return 장바구니 목록
     */
    @PatchMapping("/minus/{productId}")
    public ResponseEntity<ApiResponse<CartDto>> minusQuantity(
            @PathVariable Long productId,
            @LoginUser Long memberId) {

        return ResponseEntity.ok(cartService.minusQuantity(productId, memberId));
    }

    /**
     * [PATCH] 장바구니의 특정 상품 수량 변경(직접입력)
     *
     * @param productId 상품 고유번호
     * @param memberId request JWT 토큰 내부의 사용자 Id값
     * @return 장바구니 목록
     */
    @PatchMapping("/{count}/{productId}")
    public ResponseEntity<ApiResponse<CartDto>> updateQuantity(
            @Min(value = 1, message = INVALID_QUANTITY_MESSAGE) @PathVariable Long count,
            @PathVariable Long productId,
            @LoginUser Long memberId) {

        return ResponseEntity.ok(cartService.updateQuantity(productId, count, memberId));
    }

    /**
     * [DELETE] 장바구니 상품 삭제 (개별)
     *
     * @param productId 상품 고유번호
     * @param memberId request JWT 토큰 내부의 사용자 Id값
     * @return 장바구니 목록
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<List<CartDto>>> deleteProduct(
            @PathVariable Long productId,
            @LoginUser Long memberId) {

        return ResponseEntity.ok(cartService.deleteProduct(productId, memberId));
    }

    /**
     * [DELETE] 장바구니 상품 삭제 (전체)
     *
     * @param memberId request JWT 토큰 내부의 사용자 Id값
     * @return 빈 장바구니 목록
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<List<CartDto>>> deleteAllProduct(@LoginUser Long memberId) {

        return ResponseEntity.ok(cartService.deleteAllProduct(memberId));
    }
}
