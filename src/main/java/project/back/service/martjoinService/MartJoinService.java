package project.back.service.martjoinService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import project.back.dto.MartJoinContentDto;
import project.back.dto.MartLocationDto;
import project.back.dto.MartJoinContentDto;
import project.back.entitiy.Member;
import project.back.repository.memberrepository.MemberRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class MartJoinService {


    @Value("${kakao.rest.api.key}")
    private String restApiKey;


    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;

    //memberId를 통해 주소 찾아내기
    public String findAddress(Long memberId){
        Member member = memberRepository.findByMemberId(memberId);
        return member.getAddress();    // memberId에 해당하는 Member는 무조건 있다고 생각해 예외처리 x
    }

    //주소를 받아서 카카오api 사용해서  위도와 경도 찾아내는 것
    public MartLocationDto findLatitudeLongitude(String address){
        String url = "https://dapi.kakao.com/v2/local/search/address.json" + "?query=" + address;;

        //헤더 만들기
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "KakaoAk " + restApiKey);

        //엔티티 만들기
        HttpEntity<String> entity = new HttpEntity<>(headers);

        //restTemplate 이용
        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        // 응답 처리
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = responseEntity.getBody();
            if (responseBody != null && responseBody.containsKey("documents")) {
                List<Map<String, Object>> documents = (List<Map<String, Object>>) responseBody.get("documents");
                if (!documents.isEmpty()) {
                    Map<String, Object> document = documents.get(0);
                    Map<String, Object> addressInfo = (Map<String, Object>) document.get("address");
                    double latitude = (double) addressInfo.get("y");
                    double longitude =  (double) addressInfo.get("x");

                    MartLocationDto martLocationDto = MartLocationDto.builder()
                            .latitude(latitude)
                            .longitude(longitude)
                            .build();
                    return martLocationDto;
                }
            }
        }
        return null;

    }

    //위도와 경도를 기준으로 전방 200m(임의)안에 마트조인 하는 부분
    public List<MartJoinContentDto> searchMarts(double latitude, double longitude, int radius) {
        String url = "https://dapi.kakao.com/v2/local/search/category.json" +
                "?category_group_code=MT1" +       // 여기ㅣ 부분을 카테고리 바꾸면 된다.
                "&radius=" + radius +
                "&x=" + longitude +
                "&y=" + latitude +
                "&sort=distance";  // 거리별로 정렬 기능

        //헤더 만들기
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "KakaoAK " + restApiKey);

        //엔티티 만들기  (headers) 로 헤더 정보 안에 넣고 http 응답 요청을 보낼떄 필요한 entity를 만드는것
        HttpEntity<String> entity = new HttpEntity<>(headers);

        //restTemplate 이용해서
        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        // entity사용안하고 string 으로 바로 응답받는 예시 GET 요청을 보내고 JSON 응답을 받음
//        String jsonResponse = restTemplate.getForObject(apiUrl, String.class);


//        if (responseEntity.getStatusCode() == HttpStatus.OK) {
//            Map<String, Object> responseBody = responseEntity.getBody();
//            if (responseBody != null && responseBody.containsKey("documents")) {
//                return (List<Map<String, Object>>) responseBody.get("documents");
//            }
//        }
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = responseEntity.getBody();
            if (responseBody != null && responseBody.containsKey("documents")) {
                List<Map<String, Object>> documents = (List<Map<String, Object>>) responseBody.get("documents");
                List<MartJoinContentDto> pharmacies = new ArrayList<>();

                for (Map<String, Object> document : documents) {
                    MartJoinContentDto pharmacy = MartJoinContentDto.builder()
                            //필요한거 Dto고쳐가면서 추가 해주면 된다.
                            .id( (String) document.get("id"))
                            .distance((String) document.get("distance"))
                            .placeName((String) document.get("place_name"))
                            .address((String) document.get("address_name"))
                            .roadAddress((String) document.get("road_address_name"))
                            .phone((String) document.get("phone"))
                            .build();

                    pharmacies.add(pharmacy);
                }
                return pharmacies;
            }
        }

        return Collections.emptyList();
    }

}
