package project.back.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.back.etc.enums.cart.CartErrorMessage;


@Entity
@Table(name = "cart")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;

    @Column(name = "quantity")
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    /* 더미 데이터 삽입에 사용 */
    public Cart(Long quantity, Member member, Product product) {
        this.quantity = quantity;
        this.member = member;
        this.product = product;
    }

    public void plusQuantity(){
        this.quantity++;
    }

    public void minusQuantity(){
        if(this.quantity<2){
            throw new IllegalArgumentException(
                    CartErrorMessage.QUANTITY_ONE_OR_MORE.getMessage()+CartErrorMessage.DELETE_RECOMMEND.getMessage()
            );
        }
        this.quantity--;
    }

    public void updateQuantity(long quantity) {
        if(quantity<1){
            throw new IllegalArgumentException(CartErrorMessage.QUANTITY_ONE_OR_MORE.getMessage());
        }
        this.quantity = quantity;
    }
}
