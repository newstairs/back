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
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = true)
    private String productImgUrl;

    /* 더미 데이터 삽입에 사용 */
    public Product(String productName, String productImgUrl) {
        this.productName = productName;
        this.productImgUrl = productImgUrl;
    }
}
