package project.back.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ProductDto {
    private String productName;
    private String productImgUrl;
}
