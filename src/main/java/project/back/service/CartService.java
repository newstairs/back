package project.back.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.back.dto.ApiResponse;
import project.back.dto.ProductDto;
import project.back.entity.Product;
import project.back.repository.CartRepository;
import project.back.repository.ProductRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final String NOT_EXIST_PRODUCT = "존재하지 않는 상품입니다.";
    private final String SUCCESS_SEARCH = "검색에 성공 했습니다.";

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    /**
     * 상품 검색
     *
     * @param productName 상품이름(String)
     * @return 상품이름을 포함하는 상품들(과 이미지)
     * @throws RuntimeException 상품이름을 포함하는 이름이 없는경우
     */
    // TODO: 일단 Runtime하고 예외처리 규칙 정하고 하자 <br>
    // TODO: imageUrl은 어떻게 할건지 물어보기, 추가한다면 builder에서 요소 추가
    public ApiResponse<List<ProductDto>> findAllByProductName(String productName) {
        Optional<List<Product>> allByProductName = productRepository.findAllByProductNameContaining(productName);
        log.info("productName: {}, allByProductName: {}", productName, allByProductName);

        List<Product> products = allByProductName.get();

        if(products.isEmpty()){
            throw new NoSuchElementException(NOT_EXIST_PRODUCT);
        }

        List<ProductDto> productDtos = products.stream()
                .map( product -> ProductDto.builder()
                                .productName(product.getProductName())
                                .build())
                .toList();

        return ApiResponse.success(productDtos, SUCCESS_SEARCH);
    }
}
