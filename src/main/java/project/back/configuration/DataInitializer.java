package project.back.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import project.back.entitiy.Product;
import project.back.repository.ProductRepository;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 더미 데이터 삽입
        List<Product> products = Arrays.asList(
                new Product("당근"),
                new Product("떡볶이떡"),
                new Product("떡국떡"),
                new Product("고추장"),
                new Product("간장"),
                new Product("설탕"),
                new Product("어묵"),
                new Product("감자"),
                new Product("고구마"),
                new Product("대파"),
                new Product("쪽파"),
                new Product("양파"),
                new Product("마늘"),
                new Product("다진마늘")
        );
        productRepository.saveAll(products);
    }
}
