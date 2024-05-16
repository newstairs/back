package project.back.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.back.dto.ApiResponse;
import project.back.dto.CartProductDto;
import project.back.dto.ProductDto;
import project.back.dto.ProductSearchDto;
import project.back.entity.Cart;
import project.back.entity.Member;
import project.back.entity.Product;
import project.back.etc.aboutlogin.JwtUtill;
import project.back.etc.commonException.ConflictException;
import project.back.etc.commonException.NoContentFoundException;
import project.back.repository.CartRepository;
import project.back.repository.ProductRepository;
import project.back.repository.memberrepository.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final String NOT_EXIST_PRODUCT = "'%s'는 존재하지 않는 상품입니다.";
    private final String NOT_FOUND_MEMBER = "사용자 정보를 찾을 수 없습니다.";
    private final String NOT_FOUND_PRODUCT = "상품을 찾을 수 없습니다.";
    private final String SUCCESS_SEARCH = "검색에 성공 했습니다.";
    private final String ALREADY_EXIST_PRODUCT = "이미 장바구니에 존재하는 상품입니다.";
    private final String SUCCESS_ADD_PRODUCT= "장바구니에 '%s'이(가) 담겼습니다.";
    private final Long FIRST_ADD_VALUE = 1L;

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    /**
     * 상품 검색
     *
     * @param productName 상품이름(String)
     * @return 상품이름을 포함하는 상품들(과 이미지)
     * @throws NoContentFoundException productName을 포함하는 상품이 없는경우
     */
    public ApiResponse<List<ProductSearchDto>> findAllByProductName(String productName) {
        Optional<List<Product>> allByProductName = productRepository.findAllByProductNameContaining(productName);
        log.info("productName: {}, allByProductName: {}", productName, allByProductName);

        List<Product> products = allByProductName.get();

        if(products.isEmpty()){
            throw new NoContentFoundException(String.format(NOT_EXIST_PRODUCT, productName));
        }

        List<ProductSearchDto> ProductSearchDtos = products.stream()
                .map( product -> ProductSearchDto.builder()
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .productImgUrl(product.getProductImgUrl())
                        .build())
                .toList();

        return ApiResponse.success(ProductSearchDtos, SUCCESS_SEARCH);
    }

    /**
     * 상품을 장바구니에 저장(등록)
     * @param cartProductDto 카트에 담길 상품 정보
     * @param memberId 유저정보를 가져오기위한 memberId
     * @return 등록된 Cart의 정보
     * @throws EntityNotFoundException 사용자 정보나 상품 정보를 찾을 수 없는 경우
     * @throws ConflictException 같은 상품이 이미 담겨있는 경우
     */
    public ApiResponse<CartProductDto> addProduct(CartProductDto cartProductDto, Long memberId){
        Member member = getMemberByMemberId(memberId);
        Product product = getProductByProductId(cartProductDto.getProductId());
        // 이미 장바구니에 같은 상품이 담겨있는 경우
        cartRepository.findByMemberEqualsAndProductEquals(member, product)
                .ifPresent(c -> {throw new ConflictException(ALREADY_EXIST_PRODUCT);});

        Cart cart = Cart.builder()
                .product(product)
                .member(member)
                .quantity(FIRST_ADD_VALUE)
                .build();

        Cart result = cartRepository.save(cart);

        CartProductDto cartProductDtoResult = CartProductDto.builder()
                .productId(result.getProduct().getProductId())
                .quantity(result.getQuantity())
                .build();

        return ApiResponse.success(
                cartProductDtoResult, String.format(SUCCESS_ADD_PRODUCT, product.getProductName())
        );
    }

    private Member getMemberByMemberId(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MEMBER));
        return member;
    }

    private Product getProductByProductId(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_PRODUCT));
        return product;
    }
}
