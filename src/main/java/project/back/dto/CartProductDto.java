package project.back.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartProductDto {
    private Long quantity;
    private Long memberId;
    private Long productId;

    public CartProductDto(Long quantity, Long memberId, Long productId) {
        this.quantity = quantity;
        this.memberId = memberId;
        this.productId = productId;
    }
}
