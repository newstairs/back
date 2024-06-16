package project.back.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import project.back.dto.ApiResponse;
import project.back.dto.product.ProductDto;
import project.back.entity.product.Product;
import project.back.etc.commonException.NoContentFoundException;
import project.back.etc.martproduct.MartAndProductMessage;
import project.back.repository.product.ProductRepository;
import project.back.service.product.ProductService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    private final int page = 0;
    private final int size = 2;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;
    private Pageable pageable;
    private Product product1;
    private Product product2;

    @BeforeEach
    void 초기_설정() {
        pageable = PageRequest.of(page, size, Sort.by("productId").ascending());

        product1 = Product.builder().productId(1L).productName("자유시간").productImgUrl("/freetime.jpeg").build();
        product2 = Product.builder().productId(2L).productName("아이스티").productImgUrl("/null.png").build();
    }

    @Test
    void 상품_목록_출력테스트() {
        Page<Product> productPage = new PageImpl<>(List.of(product1, product2), pageable, size);
        when(productRepository.findAll(pageable)).thenReturn(productPage);

        ApiResponse<List<ProductDto>> response = productService.findAllProductDtos(page, size);

        assertThat(response.getData()).hasSize(2);
        assertThat(response.getData().get(0).getProductName()).isEqualTo("자유시간");
        assertThat(response.getData().get(1).getProductName()).isEqualTo("아이스티");
        assertThat(response.getData().get(0).getProductImgUrl()).startsWith("data:image/jpeg;base64,");
        assertThat(response.getData().get(1).getProductImgUrl()).startsWith("data:image/jpeg;base64,");
        assertThat(response.getMessage()).isEqualTo(MartAndProductMessage.LOADED_PRODUCT.getMessage());
    }

    @Test
    @DisplayName("상품 목록이 비어있는 경우")
    void 상품_목록_예외테스트() {
        Page<Product> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(productRepository.findAll(pageable)).thenReturn(emptyPage);

        NoContentFoundException e = assertThrows(NoContentFoundException.class,
                () -> productService.findAllProductDtos(page, size));
        assertThat(e.getMessage()).isEqualTo(MartAndProductMessage.EMPTY_PRODUCT_LIST.getMessage());
    }
}
