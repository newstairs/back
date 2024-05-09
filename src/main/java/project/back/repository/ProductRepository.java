package project.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.back.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
