package project.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.back.dto.DiscountInfoDto;
import project.back.entity.Mart;
import project.back.entity.Product;
import project.back.repository.CartRepository;
import project.back.repository.MartProductRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MartProductService {

    private final MartProductRepository martProductRepository;
    private final CartRepository cartRepository;

    /** 장바구니에 있는 모든 상품이 존재하는 마트 목록 + 마트별 합계 출력 */
    @Transactional(readOnly = true)
    public Map<Long, Long> findMartsByProductIds() {
        // TODO 헤더에서 memberId 가져오는 코드로 수정
        Long memberId = 1L;
        // 장바구니에 있는 상품 목록 가져오기
        List<Product> productList = cartRepository.findProductsByMemberMemberId(memberId);
        Long numberOfProducts = (long) productList.size();

        List<Mart> martList = martProductRepository.findMartsSellingAllProducts(productList, numberOfProducts);

        // 마트별로 할인 적용하여 상품 가격 출력
        List<DiscountInfoDto> discountInfoList = martProductRepository.findDiscountsByProductsAndMarts(productList, martList);
        // 마트별로 합계 출력(martId, totalPrice)
        return calculateTotalFinalPriceByMart(discountInfoList);
    }

    private Map<Long, Long> calculateTotalFinalPriceByMart(List<DiscountInfoDto> discountInfoDto) {
        return discountInfoDto.stream()
                .collect(Collectors.groupingBy(
                        DiscountInfoDto::getMartId,
                        Collectors.summingLong(DiscountInfoDto::getFinalPrice)
                ));
    }
}
