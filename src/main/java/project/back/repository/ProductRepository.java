package project.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.back.entitiy.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
