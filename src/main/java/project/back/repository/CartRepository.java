package project.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.back.entity.Cart;
import project.back.entity.Product;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c.product FROM Cart c WHERE c.member.memberId = :memberId")
    List<Product> findProductsByMemberMemberId(Long memberId);
}
