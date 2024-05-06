package project.back.service.mart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import project.back.entitiy.Pharmacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PharmacyService {

    @Value("${kakao.rest.api.key}")
    private String restApiKey;

    private final RestTemplate restTemplate;

    public List<Pharmacy> searchPharmacies(double latitude, double longitude, int radius) {
        String url = "https://dapi.kakao.com/v2/local/search/category.json" +
                "?category_group_code=MT1" +       // 여기ㅣ 부분을 카테고리 바꾸면 된다.
                "&radius=" + radius +
                "&x=" + longitude +
                "&y=" + latitude +
                "&sort=distance";  // 거리별로 정렬 기능

        //헤더 만들기
        HttpHeaders headers = new org.springframework.http.HttpHeaders();
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
                List<Pharmacy> pharmacies = new ArrayList<>();

                for (Map<String, Object> document : documents) {
                    Pharmacy pharmacy = Pharmacy.builder()
                            .id( (String) document.get("id"))
                            .distance((String) document.get("distance"))
                            .placeName((String) document.get("place_name"))
                            .address((String) document.get("address_name"))
                            .roadAddress((String) document.get("road_address_name"))
                            .phone((String) document.get("phone"))
                            .build();
//                    log.info("id={}", document.get("id"));
//                    Pharmacy pharmacy = new Pharmacy();
//                    pharmacy.setPlaceName((String) document.get("place_name"));
//                    pharmacy.setAddress((String) document.get("address_name"));
//                    pharmacy.setRoadAddress((String) document.get("road_address_name"));
//                    pharmacy.setPhone((String) document.get("phone"));


                    pharmacies.add(pharmacy);
                }
                return pharmacies;
            }
        }

        return Collections.emptyList();
    }

}
