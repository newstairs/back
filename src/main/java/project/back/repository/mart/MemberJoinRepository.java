package project.back.repository.mart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.back.entity.member.Member;

@Repository
public interface MemberJoinRepository extends JpaRepository<Member,Long> {
}
