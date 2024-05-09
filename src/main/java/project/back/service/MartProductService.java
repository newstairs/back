package project.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.back.entity.Mart;
import project.back.entity.Product;
import project.back.repository.CartRepository;
import project.back.repository.MartProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MartProductService {

    private final MartProductRepository martProductRepository;
    private final CartRepository cartRepository;

    /** 장바구니에 있는 모든 상품이 존재하는 마트 목록 + 마트별 합계 출력 */
    @Transactional(readOnly = true)
    public List<Mart> findMartsByProductIds() {
        Long memberId = 1L;
        // 장바구니에 있는 상품 목록 가져오기
        List<Product> productList = cartRepository.findProductsByMemberMemberId(memberId);
        Long numberOfProducts = (long) productList.size();

        // 장바구니에 있는 목록이 있는 마트 조회
        return martProductRepository.findMartsSellingAllProducts(productList, numberOfProducts);
    }
}
