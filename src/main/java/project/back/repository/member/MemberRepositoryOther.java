package project.back.repository.member;

import project.back.dto.member.MemberDto;
import project.back.entity.member.Member;

import java.util.Optional;

public interface MemberRepositoryOther {


    Long membersave(MemberDto m);

    Optional<Member> findmemberbyemail(String email);
}
