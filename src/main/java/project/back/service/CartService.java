package project.back.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.back.dto.ApiResponse;
import project.back.dto.CartDto;
import project.back.dto.ProductSearchDto;
import project.back.entity.Cart;
import project.back.entity.Member;
import project.back.entity.Product;
import project.back.etc.commonException.ConflictException;
import project.back.etc.commonException.NoContentFoundException;
import project.back.etc.enums.cart.CartErrorMessage;
import project.back.etc.enums.cart.CartSuccessMessage;
import project.back.etc.enums.cart.quantityUpdateSign;
import project.back.repository.CartRepository;
import project.back.repository.ProductRepository;
import project.back.repository.memberrepository.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private static final Long FIRST_ADD_VALUE = 1L;

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    /**
     * 장바구니 목록 조회
     * @param memberId 사용자정보
     * @return 장바구니 목록
     * @throws EntityNotFoundException 사용자정보를 찾을 수 없는경우
     */
    @Transactional
    public ApiResponse<List<CartDto>> getCartsByMemberId(Long memberId){
        Member member = getMemberByMemberId(memberId);
        List<CartDto> cartDtos = cartRepository.findByMemberEquals(member).stream()
                .map(cart -> CartDto.builder()
                        .productId(cart.getProduct().getProductId())
                        .productName(cart.getProduct().getProductName())
                        .productImgUrl(cart.getProduct().getProductImgUrl())
                        .quantity(cart.getQuantity())
                        .build()
                )
                .toList();
        return ApiResponse.success(cartDtos, CartSuccessMessage.GET.getMessage());
    }
    /**
     * 상품 검색
     * @param productName 상품이름(String)
     * @return 상품이름을 포함하는 상품들(과 이미지)
     * @throws NoContentFoundException productName을 포함하는 상품이 없는경우
     */
    public ApiResponse<List<ProductSearchDto>> findAllByProductName(String productName) {
        List<Product> products = productRepository.findAllByProductNameContaining(productName);
        log.info("productName: {}, products: {}", productName, products);

        if(products.isEmpty()){
            throw new NoContentFoundException(
                    String.format(CartErrorMessage.NOT_EXIST_PRODUCT.getMessage(), productName));
        }

        List<ProductSearchDto> ProductSearchDtos = products.stream()
                .map( product -> ProductSearchDto.builder()
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .productImgUrl(product.getProductImgUrl())
                        .build())
                .toList();

        return ApiResponse.success(ProductSearchDtos, CartSuccessMessage.SEARCH.getMessage());
    }

    /**
     * 상품을 장바구니에 저장(등록)
     * @param cartDto 카트에 담길 상품 정보(productId를 가진 객체)
     * @param memberId 유저정보를 가져오기위한 memberId
     * @return 장바구니 목록(CartDto)
     * @throws EntityNotFoundException 사용자 정보나 상품 정보를 찾을 수 없는 경우
     * @throws ConflictException 같은 상품이 이미 담겨있는 경우
     */
    @Transactional
    public ApiResponse<List<CartDto>> addProduct(CartDto cartDto, Long memberId){
        Member member = getMemberByMemberId(memberId);
        Product product = getProductByProductId(cartDto.getProductId());
        // 이미 장바구니에 같은 상품이 담겨있는 경우
        cartRepository.findByMemberEqualsAndProductEquals(member, product)
                .ifPresent(c -> {throw new ConflictException(CartErrorMessage.ALREADY_EXIST_PRODUCT.getMessage());});

        Cart cart = Cart.builder()
                .product(product)
                .member(member)
                .quantity(FIRST_ADD_VALUE)
                .build();

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


        return ApiResponse.success(
                cartDtos,
                String.format(CartSuccessMessage.ADD_PRODUCT.getMessage(), cart.getProduct().getProductName())
        );
    }

    /**
     * 수량 수정
     * @param productId
     * @param sign "+", "-", "숫자"
     * @param memberId
     * @return 장바구니 목록(CartDto)
     * @throws EntityNotFoundException 사용자 정보나 상품 정보를 찾을 수 없는 경우, 장바구니에 해당 상품이 존재하지 않는경우
     * @throws IllegalArgumentException "-"한 값이 0일 경우, "직접입력"한 값이 0이하인 경우, quantityChange에 이상한 값을 넣었을 경우
     */
    @Transactional
    public ApiResponse<List<CartDto>> updateQuantity(Long productId, String sign, Long memberId){
        Member member = getMemberByMemberId(memberId);
        Product product = getProductByProductId(productId);

        Cart cart = cartRepository.findByMemberEqualsAndProductEquals(member, product)
                .orElseThrow(() -> new EntityNotFoundException(CartErrorMessage.NOT_EXIST_PRODUCT_IN_CART.getMessage()));
        // TODO: "+", "-", Enum이 더 나은것인가, 하드코딩이 나은가
        if(sign.equals(quantityUpdateSign.MINUS.getValue())){
            cart.minusQuantity();
        }
        if(sign.equals(quantityUpdateSign.PLUS.getValue())){
            cart.plusQuantity();
        }
        if(!sign.equals(quantityUpdateSign.MINUS.getValue()) && !sign.equals(quantityUpdateSign.PLUS.getValue())){
            Long quantity = Optional.ofNullable(sign)
                    .map(Long::parseLong)
                    .orElseThrow(() -> new IllegalArgumentException(CartErrorMessage.INVALID_QUANTITY.getMessage()));
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

        return ApiResponse.success(cartDtos,
                String.format(CartSuccessMessage.UPDATE_CART.getMessage(), cart.getProduct().getProductName()));
    }

    // member 검증 및 객체가져오는 메서드
    private Member getMemberByMemberId(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(CartErrorMessage.NOT_FOUND_MEMBER.getMessage()));
        return member;
    }
    // product 검증 및 객체가져오는 메서드
    private Product getProductByProductId(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(CartErrorMessage.NOT_FOUND_PRODUCT.getMessage()));
        return product;
    }
}
