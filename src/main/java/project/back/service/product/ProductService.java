package project.back.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import project.back.dto.ApiResponse;
import project.back.dto.cart.ProductSearchDto;
import project.back.dto.product.ProductDto;
import project.back.entity.product.Product;
import project.back.etc.cart.enums.CartMessage;
import project.back.etc.commonException.NoContentFoundException;
import project.back.etc.martproduct.MartAndProductMessage;
import project.back.repository.product.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 상품 목록 페이징 처리해서 가져오기
     *
     * @param page 요청할 페이지 번호
     * @param size 페이지 크기
     * @return 페이징 처리된 상품 목록을 ApiResponse 형태로 반환
     * @throws RuntimeException        이미지 파일 처리 중 오류 발생
     * @throws NoContentFoundException 상품 목록이 비어있을 때 발생
     */
    public ApiResponse<List<ProductDto>> findAllProductDtos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("productId").ascending());

        List<ProductDto> productDto = productRepository.findAll(pageable)
                .map(this::convertToProductDto)
                .toList();
        if (productDto.isEmpty()) {
            throw new NoContentFoundException(MartAndProductMessage.EMPTY_PRODUCT_LIST.getMessage());
        }
        return ApiResponse.success(productDto, MartAndProductMessage.LOADED_PRODUCT.getMessage());
    }

    /**
     * 상품 검색
     *
     * @param productName 상품이름(String)
     * @return 상품이름을 포함하는 상품들(과 이미지)
     * @throws NoContentFoundException productName 을 포함하는 상품이 없는경우
     */
    public ApiResponse<List<ProductSearchDto>> findAllByProductName(String productName) {
        List<Product> products = productRepository.findAllByProductNameContaining(productName);

        validateProducts(productName, products);

        List<ProductSearchDto> ProductSearchDtos = products.stream()
                .map(ProductSearchDto::productToSearchDto)
                .toList();

        return ApiResponse.success(ProductSearchDtos, CartMessage.SUCCESS_SEARCH.getMessage());
    }

    /**
     * 상품 객체를 ProductDto 변환
     */
    private ProductDto convertToProductDto(Product product) {
        String imageUrl = getImageUrl(product.getProductImgUrl());
        return new ProductDto(product.getProductId(), product.getProductName(), imageUrl);
    }

    /**
     * 이미지 경로로 이미지 URL 생성
     */
    private String getImageUrl(String imagePath) {
        return "/images" + (imagePath != null ? imagePath : "/null.png");
    }

    /**
     * products 검증 메서드
     */
    private void validateProducts(String productName, List<Product> products) {
        if (products.isEmpty()) {
            throw new NoContentFoundException(
                    String.format(CartMessage.NOT_EXIST_PRODUCT.getMessage(), productName));
        }
    }
}
