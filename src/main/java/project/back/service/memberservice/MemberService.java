package project.back.service.memberservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import project.back.dto.MemberDto;
import project.back.entitiy.Member;
import project.back.repository.memberrepository.MemberRepository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service

public class MemberService {


    private MemberRepository memberRepository;

    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    public MemberService(@Qualifier("redisTemplate") RedisTemplate<String,Object> redisTemplate,MemberRepository memberRepository) {
        this.redisTemplate =redisTemplate;
        this.memberRepository=memberRepository;
    }



    public Long memebersave(MemberDto member){
       return memberRepository.membersave(member);


    }

    public Optional<Member> finbdyemail(String email){


        if(redisTemplate.opsForHash().get("member",email)==null){
            Optional<Member> member=memberRepository.findmemberbyemail(email);


            redisTemplate.opsForHash().put("member",email,member);
            redisTemplate.expire("member",300,TimeUnit.SECONDS);

            //redisTemplate.opsForValue().set(id.toString(),member,500, TimeUnit.SECONDS);

            return member;
        }

        Optional<Member>member=Optional.ofNullable((Member)redisTemplate.opsForHash().get("member",email));
        return member;

    }



    public Optional<Member> findmember(Long id){

        if(redisTemplate.opsForHash().get("member",id.toString())==null){
            Optional<Member> member=memberRepository.findById(id);


            redisTemplate.opsForHash().put("member",id.toString(),member);
            redisTemplate.expire("member",300,TimeUnit.SECONDS);

            //redisTemplate.opsForValue().set(id.toString(),member,500, TimeUnit.SECONDS);

            return member;
        }

        Optional<Member>member=Optional.ofNullable((Member)redisTemplate.opsForHash().get("member",id.toString()));
        return member;

    }


}
