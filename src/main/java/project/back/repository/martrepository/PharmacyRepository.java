package project.back.repository.martrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.back.entitiy.Pharmacy;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
}
