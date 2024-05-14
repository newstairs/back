package project.back.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.util.Base64;

@Service
public class ProductService {

    /**
     * 이미지 파일을 Base64로 인코딩
     *
     * @param imagePath 이미지 파일의 경로
     * @return Base64로 인코딩하여 반환
     * @throws RuntimeException 이미지 파일 처리 중 오류 발생
     */
    public String encodeImageToBase64(String imagePath) {
        try {
            Resource resource = new ClassPathResource("static/images" + imagePath);
            byte[] imageData = StreamUtils.copyToByteArray(resource.getInputStream());
            return Base64.getEncoder().encodeToString(imageData);
        } catch (IOException e) {
            throw new RuntimeException("이미지 파일 처리 중 오류가 발생했습니다." ,e);
        }
    }
}
