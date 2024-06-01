package project.back.repository.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import project.back.entity.product.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByProductNameContaining(String productName);
}
