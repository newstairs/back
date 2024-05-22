package project.back.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import project.back.dto.MartLocationDto;
import project.back.dto.MartResponseDto;
import project.back.etc.aboutlogin.JwtUtill;
import project.back.service.martjoinservice.MartJoinService;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class MartJoinController {

    private final MartJoinService martJoinService;
    private final JwtUtill jwtUtill;

    //주소 끌고 와서 위도 경도 api를 통해서 받아온다.
    @GetMapping("/marts/marts/{member_id}")
    public ResponseEntity<Flux<MartResponseDto>> getMartPlace(HttpServletRequest req) {
//        String access_token=req.getHeader("Authorization").substring(7);
//        Long memberId = jwtUtill.getidfromtoken(access_token);
        Long memberId = 1L;

        String address = martJoinService.findAddress(memberId);
        // 더미파일에 주소 "전북 삼성동 100"로 저장
        MartLocationDto martLocationDto = martJoinService.findLatitudeLongitude(address);
        return ResponseEntity.ok(martJoinService.searchMarts(martLocationDto.getLatitude(), martLocationDto.getLongitude(), 20000));
    }
}