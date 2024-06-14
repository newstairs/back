package project.back.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.back.dto.ApiResponse;
import project.back.dto.cart.CartDto;
import project.back.entity.cart.Cart;
import project.back.entity.member.Member;
import project.back.entity.product.Product;
import project.back.etc.cart.enums.CartMessage;
import project.back.etc.commonException.ConflictException;
import project.back.repository.cart.CartRepository;
import project.back.repository.member.MemberRepository;
import project.back.repository.product.ProductRepository;
import project.back.service.cart.CartService;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    CartRepository cartRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    ProductRepository productRepository;

    @InjectMocks
    CartService cartService;

    private static final Long COMMON_NUMBER = 1L;
    private static final String TEST_PRODUCT_NAME = "테스트 상품1";

    private Long memberId;
    private Long productId;
    private CartDto cartDto;
    private Member member;
    private Product product;
    private Cart cart;

    @BeforeEach
    void setUp() {
        memberId = COMMON_NUMBER;
        productId = COMMON_NUMBER;
        cartDto = CartDto.builder()
                .productId(productId)
                .productName(TEST_PRODUCT_NAME)
                .build();
        product = Product.builder()
                .productId(productId)
                .productName(TEST_PRODUCT_NAME)
                .build();
        cart = Cart.builder()
                .cartId(COMMON_NUMBER)
                .member(member)
                .product(product)
                .quantity(COMMON_NUMBER)
                .build();
        member = new Member();
        member.setMemberId(memberId);
    }

    @Test
    @DisplayName("장바구니 조회 테스트")
    void 장바구니_조회_테스트() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.ofNullable(member));
        when(cartRepository.findByMemberEquals(member)).thenReturn(Arrays.asList(cart));

        ApiResponse<List<CartDto>> result = cartService.getCartsByMemberId(memberId);
        assertThat(result.getData().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("장바구니 조회 예외 테스트")
    void 장바구니_조회_예외_테스트_EntityNotFoundException() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.getCartsByMemberId(memberId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("상품추가 테스트")
    void 상품추가_테스트() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.ofNullable(member));
        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(product));
        when(cartRepository.findByMemberEqualsAndProductEquals(member, product)).thenReturn(Optional.empty());

        ApiResponse<CartDto> result = cartService.addProduct(cartDto, memberId);

        String expectMessage = String.format(CartMessage.SUCCESS_ADD.getMessage(), TEST_PRODUCT_NAME);
        assertThat(result.getMessage()).isEqualTo(expectMessage);
    }

    @Test
    @DisplayName("상품추가 예외테스트: 사용자 없음")
    void 상품추가_예외테스트_사용자없음_EntityNotFoundException() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.addProduct(cartDto, memberId))
                .isInstanceOf(EntityNotFoundException.class);
    }
    @Test
    @DisplayName("상품추가 예외테스트: 상품 없음")
    void 상품추가_예외테스트_상품없음_EntityNotFoundException(){
        when(memberRepository.findById(memberId)).thenReturn(Optional.ofNullable(member));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.addProduct(cartDto, memberId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("상품추가 예외테스트: 중복된 상품")
    void 상품추가_예외테스트_중복된_상품_ConflictException() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.ofNullable(member));
        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(product));
        // 이미 존재하는 상황가정
        when(cartRepository.findByMemberEqualsAndProductEquals(member, product)).thenReturn(Optional.ofNullable(cart));

        assertThatThrownBy(() -> cartService.addProduct(cartDto, memberId))
                .isInstanceOf(ConflictException.class);

    }

    @ParameterizedTest
    @DisplayName("상품 수량 변경 테스트(직접입력)")
    @CsvSource(value = {"6, 6", "5, 5", "1, 1"})
    void 상품_수량_변경_테스트(Long count, Long expected) {
        when(memberRepository.findById(memberId)).thenReturn(Optional.ofNullable(member));
        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(product));
        when(cartRepository.findByMemberEqualsAndProductEquals(member, product)).thenReturn(Optional.ofNullable(cart));

        ApiResponse<CartDto> response = cartService.updateQuantity(productId, count, memberId);
        Long result = response.getData().getQuantity();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("상품 수량 변경 테스트(증가)")
    void 상품_수량_변경_테스트_증가(){
        when(memberRepository.findById(memberId)).thenReturn(Optional.ofNullable(member));
        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(product));
        when(cartRepository.findByMemberEqualsAndProductEquals(member, product)).thenReturn(Optional.ofNullable(cart));

        ApiResponse<CartDto> response = cartService.plusQuantity(productId, memberId);
        Long result = response.getData().getQuantity();

        assertThat(result).isEqualTo(2L);
    }
    @Test
    @DisplayName("상품 수량 변경 테스트(감소)")
    void 상품_수량_변경_테스트_감소(){
        when(memberRepository.findById(memberId)).thenReturn(Optional.ofNullable(member));
        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(product));
        Cart customCart = Cart.builder()
                .cartId(COMMON_NUMBER)
                .member(member)
                .product(product)
                .quantity(6L)
                .build();
        when(cartRepository.findByMemberEqualsAndProductEquals(member, product)).thenReturn(Optional.ofNullable(customCart));

        ApiResponse<CartDto> response = cartService.minusQuantity(productId, memberId);
        Long result = response.getData().getQuantity();

        assertThat(result).isEqualTo(5L);
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1, -2})
    @DisplayName("상품 수량 변경(직접입력) 예외 테스트: 직접입력한 값이 0이하인 경우")
    void 상품_수량_변경_예외_테스트_직접입력_IllegalArgumentException(Long count) {
        when(memberRepository.findById(memberId)).thenReturn(Optional.ofNullable(member));
        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(product));
        when(cartRepository.findByMemberEqualsAndProductEquals(member, product)).thenReturn(Optional.ofNullable(cart));

        assertThatThrownBy(() -> cartService.updateQuantity(productId, count, memberId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 수량 변경(감소) 예외 테스트: -한 값이 0 인 경우")
    void 상품_수량_변경_예외_테스트_감소_IllegalArgumentException(){
        when(memberRepository.findById(memberId)).thenReturn(Optional.ofNullable(member));
        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(product));
        when(cartRepository.findByMemberEqualsAndProductEquals(member, product)).thenReturn(Optional.ofNullable(cart));

        assertThatThrownBy(() -> cartService.minusQuantity(productId, memberId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 수량 변경 예외 테스트: 장바구니에 해당 상품없음")
    void 상품_수량_변경_예외_테스트_EntityNotFoundException() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.ofNullable(member));
        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(product));
        when(cartRepository.findByMemberEqualsAndProductEquals(member, product)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.updateQuantity(productId, 1L, memberId))
                .isInstanceOf(EntityNotFoundException.class);
        assertThatThrownBy(() -> cartService.plusQuantity(productId, memberId))
                .isInstanceOf(EntityNotFoundException.class);
        assertThatThrownBy(() -> cartService.minusQuantity(productId, memberId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("(개별)상품 삭제 테스트")
    void 개별_상품_삭제_테스트() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.ofNullable(member));
        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(product));
        when(cartRepository.findByMemberEqualsAndProductEquals(member, product)).thenReturn(Optional.ofNullable(cart));

        ApiResponse<CartDto> result = cartService.deleteProduct(productId, memberId);
        String expectMessage = String.format(CartMessage.SUCCESS_DELETE.getMessage(), TEST_PRODUCT_NAME);

        assertThat(result.getMessage()).isEqualTo(expectMessage);
    }

    @Test
    @DisplayName("(개별)상품 삭제 예외 테스트: 해당 상품이 장바구니에 없는경우")
    void 개별_상품_삭제_예외_테스트_EntityNotFoundException() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.ofNullable(member));
        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(product));
        when(cartRepository.findByMemberEqualsAndProductEquals(member, product)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.deleteProduct(productId, memberId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("(전체)상품 삭제 테스트")
    void 전체_상품_삭제_테스트() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.ofNullable(member));

        ApiResponse<CartDto> result = cartService.deleteAllProduct(memberId);

        assertThat(result.getMessage()).isEqualTo(CartMessage.SUCCESS_DELETE_ALL.getMessage());

    }
}