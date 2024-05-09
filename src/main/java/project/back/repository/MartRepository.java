package project.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.back.entity.Mart;

@Repository
public interface MartRepository extends JpaRepository<Mart, Long> {
}
