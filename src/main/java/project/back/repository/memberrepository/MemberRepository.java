package project.back.repository.memberrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.back.entitiy.Member;

public interface MemberRepository extends JpaRepository<Member,Long>, MemberRepositoryOther {
    //martJoinController부분에서 사용할려고 추가
    Member findByMemberId(Long memberId);
}
