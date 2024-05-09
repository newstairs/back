package project.back.repository.martjoinrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.back.dto.MartJoinContentDto;

public interface MartJoinRepository extends JpaRepository<MartJoinContentDto, Long> {
}
