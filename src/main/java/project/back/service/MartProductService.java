package project.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.back.dto.ApiResponse;
import project.back.dto.CartProductDto;
import project.back.dto.DiscountInfoDto;
import project.back.entity.Mart;
import project.back.entity.Product;
import project.back.repository.CartRepository;
import project.back.repository.MartProductRepository;
import project.back.repository.ProductRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MartProductService {

    private final MartProductRepository martProductRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    /** 장바구니에 있는 모든 상품이 존재하는 마트 목록 + 마트별 합계 출력 */
    @Transactional(readOnly = true)
    public ApiResponse<Map<Long, Long>> findMartsByProductIds() {
        // TODO 헤더에서 memberId 가져오는 코드로 수정
        Long memberId = 1L;
        // 장바구니에 있는 상품 목록 가져오기
        List<CartProductDto> cartProductList = cartRepository.findCartsByMemberMemberId(memberId);
        List<Product> productList = cartProductList.stream()
                .map(dto -> productRepository.findById(dto.getProductId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        Long numberOfProducts = (long) productList.size();

        List<Mart> martList = martProductRepository.findMartsSellingAllProducts(productList, numberOfProducts);

        // 마트별로 할인 적용하여 상품 가격 출력
        List<DiscountInfoDto> discountInfoList = martProductRepository.findDiscountsByProductsAndMarts(productList, martList);

        // 마트별로 합계 출력(martId, totalPrice)
        return ApiResponse.success(calculateTotalFinalPriceByMart(discountInfoList, cartProductList), "마트별 총 합계를 불러왔습니다.");
    }

    private Map<Long, Long> calculateTotalFinalPriceByMart(
            List<DiscountInfoDto> discountInfoDto,
            List<CartProductDto> cartProductList
    ) {
        Map<Long, Long> productIdToQuantity = cartProductList.stream()
                .collect(Collectors.toMap(CartProductDto::getProductId, CartProductDto::getQuantity, (a, b) -> b));

        return discountInfoDto.stream()
                .filter(dto -> productIdToQuantity.containsKey(dto.getProductId()))
                .collect(Collectors.groupingBy(
                        DiscountInfoDto::getMartId,
                        Collectors.summingLong(dto -> dto.getFinalPrice() * productIdToQuantity.get(dto.getProductId()))
                ));
    }
}
