package project.back.service.mart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.back.dto.ApiResponse;
import project.back.dto.mart.MartReqDto;
import project.back.dto.mart.MartResDto;
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
     * @throws IllegalArgumentException 마트 정보가 비어있을 경우 발생
     * @throws Exception                마트 정보 저장 시 예외 발생
     */
    @Transactional
    public ApiResponse<List<MartResDto>> saveMart(List<MartReqDto> dtos) throws Exception {
        if (dtos == null || dtos.isEmpty()) {
            throw new IllegalArgumentException(MartAndProductMessage.EMPTY_MART_DETAILS.getMessage());
        }

        List<MartResDto> responses = new ArrayList<>();
        for (MartReqDto dto : dtos) {
            try {
                Mart findMart = martRepository.findByMartName(dto.getMartName()).orElse(null);
                if (findMart != null) {
                    responses.add(MartResDto.resDto(
                            findMart.getId(), findMart.getMartName(), findMart.getMartAddress()));
                } else {
                    JoinMart joinMart = martRepository.findJoinMartByNameContaining(dto.getMartName()).orElse(null);
                    Mart mart = Mart.builder()
                            .martName(dto.getMartName())
                            .martAddress(dto.getMartAddress())
                            .joinMart(joinMart)
                            .build();
                    martRepository.save(mart);

                    responses.add(MartResDto.resDto(
                            mart.getId(), mart.getMartName(), mart.getMartAddress()));
                }
            } catch (Exception e) {
                throw new Exception(MartAndProductMessage.ERROR_SAVE_MART.getMessage(), e);
            }
        }
        return ApiResponse.success(responses, MartAndProductMessage.SAVE_MART.getMessage());
    }
}
