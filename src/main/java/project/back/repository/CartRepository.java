package project.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.back.dto.CartProductDto;
import project.back.entity.Cart;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT new project.back.dto.CartProductDto(c.quantity, c.member.memberId, c.product.productId) " +
            "FROM Cart c WHERE c.member.memberId = :memberId")
    List<CartProductDto> findCartsByMemberMemberId(Long memberId);
}
