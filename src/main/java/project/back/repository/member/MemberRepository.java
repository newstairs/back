package project.back.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import project.back.entity.member.Member;

public interface MemberRepository extends JpaRepository<Member,Long>, MemberRepositoryOther {
}
