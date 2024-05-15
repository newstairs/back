package project.back.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.back.dto.ApiResponse;
import project.back.dto.CartProductDto;
import project.back.dto.ProductDto;
import project.back.entity.Product;
import project.back.etc.aboutlogin.JwtUtill;
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
        Product product1 = new Product(testProductName);
        productRepository.save(product1);

        ApiResponse<List<ProductDto>> result1 = cartService.findAllByProductName(testProductName);
        List<ProductDto> resultDatas = result1.getData();
        // "당근"을 포함하는 모든 상품을 가져오는지
        ApiResponse<List<ProductDto>> result2 = cartService.findAllByProductName("당근");
        List<ProductDto> result2Datas = result2.getData();

        for (ProductDto resultData : resultDatas) {
            System.out.println("resultDatum = " + resultData);
        }
        for (ProductDto result2Data : result2Datas) {
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

}