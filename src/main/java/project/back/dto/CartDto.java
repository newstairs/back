package project.back.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartDto {
    private Long productId;
    private String productName;
    private String productImgUrl;
    private Long quantity;
}
