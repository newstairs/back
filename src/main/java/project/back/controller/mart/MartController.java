package project.back.controller.mart;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.back.configuration.annotation.LoginUser;
import project.back.dto.ApiResponse;
import project.back.dto.mart.MartReqDto;
import project.back.dto.mart.MartResDto;
import project.back.service.mart.MartService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MartController {

    private final MartService martService;

    /**
     * 클라이언트로부터 마트 이름과 주소를 받아서 저장
     */
    @PostMapping("/marts")
    public ResponseEntity<ApiResponse<List<MartResDto>>> saveMarts(
            @LoginUser Long memberId,
            @RequestBody List<MartReqDto> martDto
    ) throws Exception {
        return ResponseEntity.ok(martService.saveMart(martDto));
    }
}