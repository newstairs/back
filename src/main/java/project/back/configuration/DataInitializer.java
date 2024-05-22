package project.back.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import project.back.entity.*;
import project.back.repository.*;

import java.util.Arrays;
import java.util.List;
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        /* 전체 상품 목록 */
        List<Product> products = Arrays.asList(
                new Product("당근", "/carrot.jpeg"),
                new Product("떡볶이떡", "/riceCake01.jpeg"),
                new Product("떡국떡", "/riceCake02.png"),
                new Product("고추장", "/redPepperPaste.jpeg"),
                new Product("간장", "/soySauce.jpeg"),
                new Product("설탕", "/sugar.jpeg"),
                new Product("어묵", "/fishCake.jpeg"),
                new Product("감자", "/potato.jpeg"),
                new Product("고구마", "/sweetPotato.jpeg"),
                new Product("대파", "/scallions.jpeg"),
                new Product("쪽파", "/leek.jpeg"),
                new Product("양파", "/onion.jpeg"),
                new Product("마늘", "/garlic.jpeg"),
                new Product("다진마늘", "/crushedGarlic.jpeg")
        );
        productRepository.saveAll(products);
    }
}
