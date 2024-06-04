package project.back.repository.mart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.back.entity.mart.Mart;
import project.back.entity.product.JoinMart;

import java.util.List;
import java.util.Optional;

@Repository
public interface MartRepository extends JpaRepository<Mart, Long> {
    @Query("""
            SELECT jm FROM JoinMart jm
            WHERE :placeName LIKE CONCAT('%', jm.store, '%')
            """)
    Optional<JoinMart> findJoinMartByNameContaining(String placeName);

    @Query("""
            SELECT m.joinMart.joinId FROM Mart m
            WHERE m.id = :martId
            """)
    Optional<Long> findJoinIdByMartId(Long martId);

    List<Mart> findByJoinMartJoinId(Long joinId);
}
