package project.back.entitiy;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="discount_id")
    private Discount discount;

}
