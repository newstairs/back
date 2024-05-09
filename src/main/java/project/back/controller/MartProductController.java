package project.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.back.entity.Mart;
import project.back.service.MartProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/marts")
public class MartProductController {

    private final MartProductService martProductService;

    /** 장바구니에 있는 모든 상품이 존재하는 마트 목록 + 마트별 합계 출력 */
    @GetMapping("/selling")
    public ResponseEntity<List<Mart>> getMartsForCart() {
        List<Mart> martList = martProductService.findMartsByProductIds();
        return ResponseEntity.ok(martList);
    }
}
