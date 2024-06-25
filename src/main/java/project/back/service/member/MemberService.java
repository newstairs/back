package project.back.service.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import project.back.dto.member.MemberDto2;
import project.back.dto.member.MemberDtoOriginLogin;
import project.back.etc.aboutlogin.NoMemberError;
import project.back.etc.aboutlogin.JwtToken;
import project.back.etc.aboutlogin.JwtUtill;
import project.back.dto.member.MemberDto;
import project.back.entity.member.Member;
import project.back.repository.member.MemberRepository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Slf4j
public class MemberService {

    @Autowired
    private @Qualifier("redisTemplate") RedisTemplate<String,Object> redisTemplate;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoclientid;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakakoredirecturi;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenuri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userinfouri;

    private final JwtUtill jwtUtill;
    private final WebClient webClient;

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(@Qualifier("redisTemplate") RedisTemplate<String,Object> redisTemplate, JwtUtill jwtUtill, WebClient webClient, MemberRepository memberRepository) {
        this.redisTemplate =redisTemplate;
        this.jwtUtill = jwtUtill;
        this.webClient = webClient;
        this.memberRepository=memberRepository;
    }

    public Long memebersave(MemberDto2 member){
       return memberRepository.membersave(member);
    }


    public void OriginMemberSave(MemberDto memberDto){
         memberRepository.membersavebyorigin(memberDto);
    }



    public List<Object> OriginTryLogin(MemberDtoOriginLogin memberDtoOriginLogin) throws IOException {
       Optional<Member> m= memberRepository.findbyoriginway2(memberDtoOriginLogin.getEmail(),memberDtoOriginLogin.getPassword());


       if(m.isPresent()) {
           List<Object> tokendata = gettokenandrespons2(m.get().getName(), m);


           return tokendata;
       }
       else{
           throw new NoMemberError();
       }





    }



    public  List<Object>  TryLogin(MultiValueMap<String, String> accessTokenParam) throws ParseException, IOException {
        log.info("accesstokenparam:{}",accessTokenParam);

        String AnwserFromApi=webClient
                .mutate()
                .baseUrl(tokenuri)
                .defaultHeader("Content-type","application/x-www-form-urlencoded;charset=utf-8")
                .build()
                .post()
                .body(BodyInserters.fromFormData(accessTokenParam))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(AnwserFromApi);

        String header = "Bearer " + jsonObject.get("access_token");
        System.out.println("header = " + header);
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);

        String userdata=webClient.mutate()
                .defaultHeader("Authorization",header)
                .baseUrl(userinfouri)
                .build()
                .get()
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("userdata:",userdata);
        JSONObject profile=(JSONObject) jsonParser.parse(userdata);

        JSONObject properties = (JSONObject) profile.get("properties");

        JSONObject kakao_account = (JSONObject) profile.get("kakao_account");

        String email = (String) kakao_account.get("email");
        String userName = (String) properties.get("nickname");

        String user_profile_image=(String) properties.get("profile_image");

        Optional<Member> member=finbdyemail(email);


        List<Object> tokendata=gettokenandresponse(email,userName,member);

        if(redisTemplate.opsForValue().get(String.format("member_kakao_token_%d",(Long)tokendata.get(1)))==null){
            redisTemplate.opsForValue().set(String.format("member_kakao_token_%d",(Long)tokendata.get(1)),jsonObject.get("access_token")
                    ,1000, TimeUnit.SECONDS);
        }
        else{
            redisTemplate.opsForValue().set(String.format("member_kakao_token_%d",(Long)tokendata.get(1)),jsonObject.get("access_token"));
        }


        return tokendata;
    }

    public Optional<Member> finbdyemail(String email) throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        if(redisTemplate.opsForHash().get("member",email)==null){

            Optional<Member> member=memberRepository.findmemberbyemail(email);

            if(member.isPresent()) {
                redisTemplate.opsForHash().put("member",email,objectMapper.writeValueAsString(member.get()));
                redisTemplate.expire("member",300,TimeUnit.SECONDS);
            }



            return member;
        }
        Member m=objectMapper.readValue(redisTemplate.opsForHash().get("member",email).toString(),Member.class);
        Optional<Member>member=Optional.ofNullable(m);
        return member;
    }


    public List<Object> gettokenandresponse(String email, String username, Optional<Member> member) throws IOException {

        ObjectMapper objectMapper=new ObjectMapper();
        if(member.isPresent()){
            Member m=member.get();

            JwtToken jwtToken=jwtUtill.genjwt(username,m.getMemberId());

            List<Object> obj=new ArrayList<>();
            obj.add(jwtToken.getAccesstoken());
            obj.add(m.getMemberId());

            return obj;
        }
        else{
            Long id=memebersave(new MemberDto2(email,username));

            JwtToken jwtToken=jwtUtill.genjwt(username,id);

            List<Object> obj=new ArrayList<>();
            obj.add(jwtToken.getAccesstoken());
            obj.add(id);

            return obj;
        }
    }
    public List<Object> gettokenandrespons2(String username, Optional<Member> member) throws IOException {

        ObjectMapper objectMapper=new ObjectMapper();

        Member m=member.get();

        JwtToken jwtToken=jwtUtill.genjwt(username,m.getMemberId());

        List<Object> obj=new ArrayList<>();
        obj.add(jwtToken.getAccesstoken());
        obj.add(m.getMemberId());

        return obj;


    }
}
