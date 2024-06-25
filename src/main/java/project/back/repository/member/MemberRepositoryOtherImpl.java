package project.back.repository.member;

import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.back.dto.member.MemberDto;
import project.back.dto.member.MemberDto2;
import project.back.entity.member.Member;
import project.back.etc.aboutlogin.IdExistError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MemberRepositoryOtherImpl implements MemberRepositoryOther {


    private final EntityManager em;



    @Override
    public Optional<Member> findmemberbyemail(String email){
        String query="select m from Member m where m.email=:email";
        List<Member> member=em.createQuery(query,Member.class)
                .setParameter("email",email)
                .getResultList();
        if(member.size()>=1){
            Optional<Member> optmember=Optional.ofNullable(member.get(0));
            return optmember;
        }

        Optional<Member> optmember=Optional.ofNullable(null);



        return optmember;

    }

    @Override
    public Long membersave(MemberDto2 m) {
        Member member=new Member(m.getEmail(),m.getName());

        member.setCreate_time(LocalDateTime.now());
        member.setUpdate_time(LocalDateTime.now());
        em.persist(member);
        return member.getMemberId();
    }


    @Override
    public Long membersavebyorigin(MemberDto m) {


        Optional<Member> members=findbyoriginway(m.getEmail());

        if(members.isEmpty()){
        Member member=new Member(m.getEmail(),m.getPassword());

        m.setName(m.getName());
        member.setCreate_time(LocalDateTime.now());
        member.setUpdate_time(LocalDateTime.now());
        em.persist(member);
        return member.getMemberId();}

        throw new IdExistError();


    }


    @Override
    public Optional<Member> findbyoriginway(String email) {

        List<Member> m=em.createQuery("select m from member where m.email=:email ",Member.class)
                .setParameter("email",email)
                .getResultList();
        if(m.size()>=1){
            Optional<Member> optmember=Optional.ofNullable(m.get(0));
            return optmember;
        }

        Optional<Member> optmember=Optional.ofNullable(null);



        return optmember;

    }


    @Override
    public Optional<Member> findbyoriginway2(String email, String password) {
        List<Member> m=em.createQuery("select m from member where m.email=:email and m.password=:password",Member.class)
                .setParameter("email",email)
                .getResultList();
        if(m.size()>=1){
            Optional<Member> optmember=Optional.ofNullable(m.get(0));
            return optmember;
        }

        Optional<Member> optmember=Optional.ofNullable(null);



        return optmember;
    }
}
