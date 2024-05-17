package project.back.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CartTest {

    private Cart cart;

    @BeforeEach
    void setUp(){
        cart = Cart.builder().quantity(2L).build();
    }

    @Test
    @DisplayName("수량 감소 테스트")
    void 수량_감소_테스트(){
        cart.minusQuantity();

        assertThat(cart.getQuantity()).isEqualTo(1L);
    }

    @Test
    @DisplayName("수량 증가 테스트")
    void 수량_증가_테스트(){
        cart.plusQuantity();

        assertThat(cart.getQuantity()).isEqualTo(3L);
    }

    @Test
    @DisplayName("수량 직접입력 테스트")
    void 수량_직접입력_테스트(){
        Long changeQauntity = 5L;

        cart.updateQuantity(changeQauntity);

        assertThat(cart.getQuantity()).isEqualTo(5L);
    }
}