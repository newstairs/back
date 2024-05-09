package project.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountInfoDto {
    private Long martProductId;
    private Long martId;
    private Long stock;
    private BigDecimal price;
    private BigDecimal discountRate;
    private Long finalPrice;

    public DiscountInfoDto(Long martProductId,Long martId, Long stock, Long price, BigDecimal discountRate) {
        this.martProductId = martProductId;
        this.martId = martId;
        this.stock = stock;
        this.price = BigDecimal.valueOf(price);
        this.discountRate = (discountRate != null) ? discountRate : BigDecimal.ZERO;
        this.finalPrice = calculateFinalPrice(BigDecimal.valueOf(price), this.discountRate);
    }

    private Long calculateFinalPrice(BigDecimal price, BigDecimal discountRate) {
        BigDecimal discount = discountRate.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal finalPrice = price.subtract(price.multiply(discount));
        return finalPrice.setScale(0, RoundingMode.HALF_UP).longValue();
    }
}
