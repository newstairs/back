package project.back.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.back.entity.product.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
