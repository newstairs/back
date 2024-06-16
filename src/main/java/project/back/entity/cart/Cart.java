package project.back.entity.cart;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.back.entity.member.Member;
import project.back.entity.product.Product;
import project.back.etc.cart.enums.CartMessage;

@Entity
@Table(name = "cart")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {

    private static final int UPDATE_MINIMUM_QUANTITY = 1;
    private static final int MINUS_MINIMUM_QUANTITY = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;

    @Column(name = "quantity")
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public void plusQuantity() {
        this.quantity++;
    }

    public void minusQuantity() {
        if (this.quantity < MINUS_MINIMUM_QUANTITY) {
            throw new IllegalArgumentException(
                    CartMessage.QUANTITY_ONE_OR_MORE.getMessage() + CartMessage.DELETE_RECOMMEND.getMessage()
            );
        }
        this.quantity--;
    }

    public void updateQuantity(Long quantity) {
        if (quantity < UPDATE_MINIMUM_QUANTITY) {
            throw new IllegalArgumentException(CartMessage.QUANTITY_ONE_OR_MORE.getMessage());
        }
        this.quantity = quantity;
    }
}
