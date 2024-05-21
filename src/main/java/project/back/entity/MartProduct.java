package project.back.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MartProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long martProductId;

    @Column(nullable = false)
    private Long stock;

    @Column(nullable = false)
    private Long price;

    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name="mart_id")
    private Mart mart;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name="discount_id")
    private Discount discount;

    /* 더미 데이터 삽입에 사용 */
    public MartProduct(Long stock, Long price, Product product, Mart mart, Discount discount) {
        this.stock = stock;
        this.price = price;
        this.product = product;
        this.mart = mart;
        this.discount = discount;
    }
}
