package project.back.repository.cart;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.back.dto.product.CartProductDto;
import project.back.entity.cart.Cart;
import project.back.entity.member.Member;

import java.util.List;

import project.back.entity.product.Product;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("""
             SELECT new project.back.dto.product.CartProductDto(c.quantity, c.member.memberId, c.product.productId)
             FROM Cart c
             WHERE c.member = :member
             """)
    List<CartProductDto> findCartsByMember(Member member);

    Optional<Cart> findByMemberEqualsAndProductEquals(Member member, Product product);

    List<Cart> findByMemberEquals(Member member);

    void deleteAllByMember(Member member);
}
