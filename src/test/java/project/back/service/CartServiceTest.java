package project.back.service;

import static org.assertj.core.api.Assertions.*;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.back.dto.ApiResponse;
import project.back.dto.CartProductDto;
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

        for (ProductSearchDto resultData : resultDatas) {
            System.out.println("resultDatum = " + resultData);
        }
        for (ProductSearchDto result2Data : result2Datas) {
            System.out.println("result2Data = " + result2Data);
        }

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
        CartProductDto cartProductDto = CartProductDto.builder()
                .productId(productId)
                .build();

        ApiResponse<CartProductDto> addResult = cartService.addProduct(cartProductDto, memberId);

        assertThat(addResult.getData().getProductId()).isEqualTo(productId);
        assertThat(addResult.getData().getQuantity()).isEqualTo(1L);
    }

    @Test
    @DisplayName("상품추가 예외테스트: 사용자 없음 ")
    void 상품추가_사용자_없음_예외테스트(){
        Long memberId = 3L;
        Long productId = 1L;

        CartProductDto cartProductDto = CartProductDto.builder()
                .productId(productId)
                .build();

        assertThatThrownBy(() -> cartService.addProduct(cartProductDto, memberId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("상품추가 예외테스트: 상품 없음")
    void 상품추가_상품_없음_예외테스트(){
        Long memberId = 1L;
        Long productId = 10000L;

        CartProductDto cartProductDto = CartProductDto.builder()
                .productId(productId)
                .build();

        assertThatThrownBy(() -> cartService.addProduct(cartProductDto, memberId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("상품추가 예외테스트: 중복된 상품")
    @Transactional
    void 상품추가_중복된_상품_예외테스트(){
        Long memberId = 1L;
        Long productId = 1L;

        CartProductDto cartProductDto = CartProductDto.builder()
                .productId(productId)
                .build();

        cartService.addProduct(cartProductDto, memberId); // 첫번째 저장

        assertThatThrownBy(() -> cartService.addProduct(cartProductDto, memberId)) // 두번째 저장 -> ConflictException
                .isInstanceOf(ConflictException.class);

    }
}