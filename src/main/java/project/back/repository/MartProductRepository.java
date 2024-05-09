package project.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.back.entity.MartProduct;

@Repository
public interface MartProductRepository extends JpaRepository<MartProduct, Long> {
}
