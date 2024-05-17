package project.back.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.back.dto.ApiResponse;
import project.back.dto.CartDto;
import project.back.dto.CartProductDto;
import project.back.dto.ProductSearchDto;
import project.back.entity.Cart;
import project.back.entity.Member;
import project.back.entity.Product;
import project.back.etc.commonException.ConflictException;
import project.back.etc.commonException.NoContentFoundException;
import project.back.etc.enums.cart.quantityUpdateSign;
import project.back.repository.CartRepository;
import project.back.repository.ProductRepository;
import project.back.repository.memberrepository.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private static final String NOT_EXIST_PRODUCT = "'%s'는 존재하지 않는 상품입니다.";
    private static final String NOT_FOUND_MEMBER = "사용자 정보를 찾을 수 없습니다.";
    private static final String NOT_FOUND_PRODUCT = "상품을 찾을 수 없습니다.";
    private static final String SUCCESS_SEARCH = "검색에 성공 했습니다.";
    private static final String ALREADY_EXIST_PRODUCT = "이미 장바구니에 존재하는 상품입니다.";
    private static final String SUCCESS_ADD_PRODUCT= "장바구니에 '%s'이(가) 담겼습니다.";
    private static final String NOT_EXIST_PRODUCT_IN_CART = "장바구니에 존재하지 않는 상품입니다.";
    private static final String INVALID_QUANTITY_MESSAGE = "1 이상의 숫자만 입력해주세요";
    private static final String SUCCESS_UPDATE_CART = "'%s'의 수량을 변경했습니다.";
    private static final Long FIRST_ADD_VALUE = 1L;

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
        //TODO: 그냥 List로 받아오게 수정하기(productRepository)
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

    /**
     * 수량 수정
     * @param productId
     * @param quantityChange "+", "-", "숫자"
     * @param memberId
     * @return 장바구니 목록(CartDto)
     * @throws EntityNotFoundException 사용자 정보나 상품 정보를 찾을 수 없는 경우, 장바구니에 해당 상품이 존재하지 않는경우
     * @throws IllegalArgumentException "-"한 값이 0일 경우, "직접입력"한 값이 0이하인 경우, quantityChange에 이상한 값을 넣었을 경우
     */
    public ApiResponse<List<CartDto>> updateQuantity(Long productId, String quantityChange, Long memberId){
        Member member = getMemberByMemberId(memberId);
        Product product = getProductByProductId(productId);

        Cart cart = cartRepository.findByMemberEqualsAndProductEquals(member, product)
                .orElseThrow(() -> new EntityNotFoundException(NOT_EXIST_PRODUCT_IN_CART));
        // TODO: "+", "-", Enum이 더 나은것인가, 하드코딩이 나은가
        if(quantityChange.equals(quantityUpdateSign.MINUS.getValue())){
            cart.minusQuantity();
        }
        if(quantityChange.equals(quantityUpdateSign.PLUS.getValue())){
            cart.plusQuantity();
        }
        if(!quantityChange.equals(quantityUpdateSign.MINUS.getValue())
                && !quantityChange.equals(quantityUpdateSign.PLUS.getValue())){
            Long quantity = Optional.ofNullable(quantityChange)
                    .map(Long::parseLong)
                    .orElseThrow(() -> new IllegalArgumentException(INVALID_QUANTITY_MESSAGE));
            cart.updateQuantity(quantity);
        }

        cartRepository.save(cart);

        List<CartDto> cartDtos = cartRepository.findByMemberEquals(member).stream()
                .map(c -> CartDto.builder()
                        .productId(c.getProduct().getProductId())
                        .productName(c.getProduct().getProductName())
                        .productImgUrl(c.getProduct().getProductImgUrl())
                        .quantity(c.getQuantity())
                        .build()
                )
                .toList();

        return ApiResponse.success(cartDtos, String.format(SUCCESS_UPDATE_CART, cart.getProduct().getProductName()));
    }

    // member 검증 및 객체가져오는 메서드
    private Member getMemberByMemberId(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MEMBER));
        return member;
    }
    // product 검증 및 객체가져오는 메서드
    private Product getProductByProductId(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_PRODUCT));
        return product;
    }
}
