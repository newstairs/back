package project.back.service.mart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.back.dto.ApiResponse;
import project.back.dto.mart.MartDto;
import project.back.entity.mart.Mart;
import project.back.entity.product.JoinMart;
import project.back.etc.martproduct.MartAndProductMessage;
import project.back.repository.mart.MartRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MartService {

    private final MartRepository martRepository;

    /**
     * 클라이언트로부터 마트 이름과 주소를 받아서 저장 및 JoinMart에 해당 마트가 있다면 연결
     *
     * @param dtos 저장할 마트 정보
     * @return MartDto 저장된 마트 정보 목록 반환
     */
    @Transactional
    public ApiResponse<List<MartDto>> saveMart(List<MartDto> dtos) {
        List<MartDto> responses = new ArrayList<>();
        for (MartDto dto : dtos) {
            JoinMart joinMart = martRepository.findJoinMartByNameContaining(dto.getMartName()).orElse(null);
            Mart mart = martRepository.save(Mart.builder()
                    .martName(dto.getMartName())
                    .martAddress(dto.getMartAddress())
                    .joinMart(joinMart)
                    .build());
            martRepository.save(mart);
            responses.add(new MartDto(mart.getMartName(), mart.getMartAddress()));
        }
        return ApiResponse.success(responses, MartAndProductMessage.SAVE_MART.getMessage());
    }
}
