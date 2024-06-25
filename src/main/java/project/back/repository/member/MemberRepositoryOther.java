package project.back.repository.member;

import project.back.dto.member.MemberDto;
import project.back.dto.member.MemberDto2;
import project.back.entity.member.Member;

import java.util.Optional;

public interface MemberRepositoryOther {


    Long membersave(MemberDto2 m);

    Optional<Member> findmemberbyemail(String email);


    Optional<Member> findbyoriginway(String email);



    Optional<Member> findbyoriginway2(String email,String password);


    Long membersavebyorigin(MemberDto m);



}
