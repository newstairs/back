package project.back.service;

import static org.assertj.core.api.Assertions.*;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.back.dto.ApiResponse;
import project.back.dto.CartDto;
import project.back.dto.ProductSearchDto;
import project.back.entity.Product;
import project.back.etc.commonException.ConflictException;
import project.back.etc.commonException.NoContentFoundException;
import project.back.repository.ProductRepository;

@SpringBootTest
class CartServiceTest {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CartService cartService;

    @Test
    @DisplayName("장바구니 조회 테스트")
    void 장바구니_조회_테스트(){
        Long memberId = 1L;

        ApiResponse<List<CartDto>> result = cartService.getCartsByMemberId(memberId);

        assertThat(result.getData()).isNotNull();
    }

    @Test
    @DisplayName("장바구니 조회 예외 테스트")
    void 장바구니_조회_예외_테스트_EntityNotFoundException(){
        Long memberId = 1000L;

        assertThatThrownBy(() -> cartService.getCartsByMemberId(memberId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("상품검색테스트")
    @Transactional
    void 재료검색테스트(){
        String testProductName = "이준오의 당근";
        Product product1 = new Product(testProductName, "temp");
        productRepository.save(product1);

        ApiResponse<List<ProductSearchDto>> result1 = cartService.findAllByProductName(testProductName);
        List<ProductSearchDto> resultDatas = result1.getData();
        // "당근"을 포함하는 모든 상품을 가져오는지
        ApiResponse<List<ProductSearchDto>> result2 = cartService.findAllByProductName("당근");
        List<ProductSearchDto> result2Datas = result2.getData();

        assertThat(resultDatas.size()).isEqualTo(1);
        assertThat(result2Datas.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("상품검색 예외테스트")
    void 상품검색_예외테스트(){
        String productName = "상품이름검색 ㅋㅋ 이런 상품은 없겠지";

        assertThatThrownBy(() -> cartService.findAllByProductName(productName))
                .isInstanceOf(NoContentFoundException.class);
    }

    @Test
    @DisplayName("상품추가 테스트")
    @Transactional
    void 상품추가_테스트(){
        Long memberId = 1L;
        Long productId = 1L;
        CartDto cartDto = CartDto.builder()
                .productId(productId)
                .build();

        ApiResponse<List<CartDto>> result = cartService.addProduct(cartDto, memberId);
        CartDto addedProduct = result.getData().stream()
                .filter(c -> c.getProductId() == productId)
                .toList()
                .get(0);

        assertThat(addedProduct).isNotNull();
    }

    @ParameterizedTest
    @CsvSource(value = {"1000,1", "1,10000"})
    @DisplayName("상품추가 예외테스트: 사용자 없음(1000), 상품 없음(10000) ")
    void 상품추가_예외테스트_EntityNotFoundException(Long memberId, Long productId){
        CartDto cartDto = CartDto.builder()
                .productId(productId)
                .build();

        assertThatThrownBy(() -> cartService.addProduct(cartDto, memberId))
                .isInstanceOf(EntityNotFoundException.class);
    }


    @Test
    @DisplayName("상품추가 예외테스트: 중복된 상품")
    @Transactional
    void 상품추가_예외테스트_중복된_상품_ConflictException(){
        Long memberId = 1L;
        Long productId = 1L;

        CartDto cartDto = CartDto.builder()
                .productId(productId)
                .build();

        cartService.addProduct(cartDto, memberId); // 첫번째 저장

        assertThatThrownBy(() -> cartService.addProduct(cartDto, memberId)) // 두번째 저장 -> ConflictException
                .isInstanceOf(ConflictException.class);

    }

    @ParameterizedTest
    @DisplayName("상품 수량 변경 테스트")
    @CsvSource(value = {"+, 7", "-, 5", "5, 5"})
    @Transactional
    void 상품_수량_변경_테스트(String quantityChange, Long expected){
        Long memberId = 1L;
        Long productId = 1L;

        CartDto cartDto = CartDto.builder().productId(productId).build();
        cartService.addProduct(cartDto, memberId); // 장바구니에 1L인 상품추가
        cartService.updateQuantity(memberId, "6", productId); // 6개로 세팅

        ApiResponse<List<CartDto>> response = cartService.updateQuantity(productId, quantityChange, memberId);
        Long result = response.getData().stream()
                .filter(c -> c.getProductId() == productId)
                .toList()
                .get(0).getQuantity();
        System.out.println("result = " + result + ", expected = " + expected);

        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"-", "0", " ", ""})
    @DisplayName("상품 수량 변경 예외 테스트: -한 값이 0인 경우, 직접입력한 값이 0인 경우, 직접입력한값이 적절하지 않은 경우")
    @Transactional
    void 상품_수량_변경_예외_테스트_IllegalArgumentException(String quantityChange){
        Long memberId = 1L;
        Long productId = 1L;

        CartDto cartDto = CartDto.builder().productId(productId).build();
        cartService.addProduct(cartDto, memberId); // 장바구니에 1L인 상품추가, quantity=1

        assertThatThrownBy(() -> cartService.updateQuantity(productId, quantityChange, memberId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"3,1,+", "1,1000,+", "1,1,+"})
    @DisplayName("상품 수량 변경 예외 테스트: 사용자 없음, 상품 없음, 장바구니에 해당 상품없음")
    @Transactional
    void 상품_수량_변경_예외_테스트_EntityNotFoundException(Long memberId, Long productId, String quantityChange) {
        assertThatThrownBy(() -> cartService.updateQuantity(productId, quantityChange, memberId))
                .isInstanceOf(EntityNotFoundException.class);
    }
}