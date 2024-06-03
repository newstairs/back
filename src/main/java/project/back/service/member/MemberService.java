package project.back.service.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
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

    public Long memebersave(MemberDto member){
       return memberRepository.membersave(member);
    }

    public  List<Object>  TryLogin(MultiValueMap<String, String> accessTokenParam) throws ParseException, IOException {
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
                .build()
                .get()
                .retrieve()
                .bodyToMono(String.class)
                .block();
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

    public Optional<Member> findmember(Long id) throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        if(redisTemplate.opsForHash().get("member",id.toString())==null){
            Optional<Member> member=memberRepository.findById(id);

            redisTemplate.opsForHash().put("member",id.toString(),objectMapper.writeValueAsString(member.get()));
            redisTemplate.expire("member",300,TimeUnit.SECONDS);

            //redisTemplate.opsForValue().set(id.toString(),member,500, TimeUnit.SECONDS);

            return member;
        }
        Optional<Member>member=Optional.ofNullable(objectMapper.readValue((String) redisTemplate.opsForHash().get("member",id.toString()),Member.class));
        return member;
    }

    public List<Object> gettokenandresponse(String email, String username, Optional<Member> member) throws IOException {
        if(member.isPresent()){
            Member m=member.get();

            JwtToken jwtToken=jwtUtill.genjwt(username,m.getMemberId());

            List<Object> obj=new ArrayList<>();
            obj.add(jwtToken.getAccesstoken());
            obj.add(m.getMemberId());

            return obj;
        }
        else{
            Long id=memebersave(new MemberDto(email,username));

            JwtToken jwtToken=jwtUtill.genjwt(username,id);

            List<Object> obj=new ArrayList<>();
            obj.add(jwtToken.getAccesstoken());
            obj.add(id);

            return obj;
        }
    }
}
