package project.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import project.back.entitiy.Pharmacy;
import project.back.service.mart.PharmacyService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @GetMapping("/pharmacies")
    public List<Pharmacy> getPharmacies() {
        // 약국 정보를 가져오는 메서드 호출
        return pharmacyService.searchPharmacies(37.504622, 127.027497, 20000);
    }
}