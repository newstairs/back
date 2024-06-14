package project.back.repository.member;

import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.back.dto.member.MemberDto;
import project.back.entity.member.Member;

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
    public Long membersave(MemberDto m) {
        Member member=new Member(m.getEmail(),m.getUsername());
        member.setCreate_time(LocalDateTime.now());
        member.setUpdate_time(LocalDateTime.now());
        em.persist(member);
        return member.getMemberId();
    }

}
