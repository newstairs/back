package project.back.controller;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import project.back.dto.MemberDto;
import project.back.entitiy.Member;
import project.back.etc.aboutlogin.Access_code;
import project.back.etc.aboutlogin.JwtToken;
import project.back.etc.aboutlogin.JwtUtill;
import project.back.etc.aboutlogin.exception.TokenSending;
import project.back.service.memberservice.MemberService;

import java.io.IOException;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class LoginController {


    private final JwtUtill jwtUtill;
    private final WebClient webClient;
    private final MemberService memberService;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoclientid;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakakoredirecturi;


    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenuri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userinfouri;




    @GetMapping("/reqlogin")
    @ResponseBody
    public ResponseEntity<TokenSending> logins(@RequestBody Access_code access_code) throws ParseException, IOException {
        String code=access_code.getAccess_code();
        MultiValueMap<String, String> accessTokenParams = accessTokenParams("authorization_code",kakaoclientid,code,kakakoredirecturi);
       MultiValueMap<String, String> accessTokenRequest = accessTokenParams;
        String answerfromapi=webClient.mutate()
                .baseUrl(tokenuri)
                .defaultHeader("Content-type","application/x-www-form-urlencoded;charset=utf-8")
                .build()
                .post()
                .body(BodyInserters.fromFormData(accessTokenRequest))
                .retrieve()
                .bodyToMono(String.class)
                .block();


        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(answerfromapi);

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

        System.out.println(properties);


        JSONObject kakao_account = (JSONObject) profile.get("kakao_account");

        String email = (String) kakao_account.get("email");
        String userName = (String) properties.get("nickname");

        String user_profile_image=(String) properties.get("profile_image");


        Optional<Member> member=memberService.finbdyemail(email);

        List<Object> tokendata=gettokenandresponse(email,userName,member);

        return new ResponseEntity<>(new TokenSending((String) tokendata.get(0)), HttpStatus.OK);
    }


    public MultiValueMap<String, String> accessTokenParams(String grantType, String clientId,String code,String redirect_uri) {
        MultiValueMap<String, String> accessTokenParams = new LinkedMultiValueMap<>();
        accessTokenParams.add("grant_type", grantType);
        accessTokenParams.add("client_id", clientId);
        accessTokenParams.add("code", code);
        accessTokenParams.add("redirect_uri", redirect_uri);
        return accessTokenParams;
    }


    public List<Object> gettokenandresponse(String email, String username, Optional<Member> member) throws IOException{
        if(member.isPresent()){
            Member m=member.get();

            JwtToken jwtToken=jwtUtill.genjwt(username,m.getMember_id());

            List<Object> obj=new ArrayList<>();
            obj.add(jwtToken.getAccesstoken());
            obj.add(m.getMember_id());


            return obj;
        }
        else{

            Long id=memberService.memebersave(new MemberDto(email,username));


            JwtToken jwtToken=jwtUtill.genjwt(username,id);

            List<Object> obj=new ArrayList<>();
            obj.add(jwtToken.getAccesstoken());
            obj.add(id);

            return obj;
        }

    }

}





