package project.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.back.entity.Mart;
import project.back.entity.MartProduct;
import project.back.entity.Product;

import java.util.List;

@Repository
public interface MartProductRepository extends JpaRepository<MartProduct, Long> {

    @Query("SELECT mp.mart FROM MartProduct mp WHERE mp.product IN :products " +
            "GROUP BY mp.mart HAVING COUNT(DISTINCT mp.product) = :numberOfProducts")
    List<Mart> findMartsSellingAllProducts(List<Product> products, Long numberOfProducts);
}
