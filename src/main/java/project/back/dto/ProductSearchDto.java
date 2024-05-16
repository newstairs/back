package project.back.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductSearchDto {
    private Long productId;
    private String productName;
    private String productImgUrl;
}
