package project.back.dto.cart;

import lombok.Builder;
import lombok.Data;
import project.back.entity.cart.Cart;

@Data
@Builder
public class CartDto {
    private Long productId;
    private String productName;
    private String productImgUrl;
    private Long quantity;

    public static CartDto CartToDto(Cart cart){
        return CartDto.builder()
                .productId(cart.getProduct().getProductId())
                .productName(cart.getProduct().getProductName())
                .productImgUrl(cart.getProduct().getProductImgUrl())
                .quantity(cart.getQuantity())
                .build();
    }
}
