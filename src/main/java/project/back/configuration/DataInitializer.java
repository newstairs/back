package project.back.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import project.back.entity.*;
import project.back.repository.*;
import project.back.repository.memberrepository.MemberRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final MartRepository martRepository;
    private final MartProductRepository martProductRepository;
    private final DiscountRepository discountRepository;
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;

    @Override
    public void run(String... args) throws Exception {
        // 더미 데이터 삽입
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

        /* 주변 마트 목록 */
        List<Mart> marts = Arrays.asList(
                new Mart("이마트", "이마트 주소"),
                new Mart("롯데마트", "롯데마트 주소"),
                new Mart("하이마트", "하이마트 주소"),
                new Mart("홈플러스", "홈플러스 주소"),
                new Mart("코스트코", "코스트코 주소"),
                new Mart("하나로마트", "하나로마트 주소")
        );
        martRepository.saveAll(marts);

        /* 할인율 */
        List<Discount> discounts = Arrays.asList(
                new Discount(BigDecimal.valueOf(10.5)), // 0
                new Discount(BigDecimal.valueOf(15.0)), // 1
                new Discount(BigDecimal.valueOf(5.0)), // 2
                new Discount(BigDecimal.valueOf(20.5)) // 3
        );
        discountRepository.saveAll(discounts);

        /* 마트별 품목 가격 및 할인율 */
        List<MartProduct> martProducts = Arrays.asList(
                // 마트 1
                new MartProduct(50L, 1500L, products.get(1), marts.get(0), discounts.get(0)),
                new MartProduct(20L, 4000L, products.get(3), marts.get(0), discounts.get(2)),
                new MartProduct(30L, 2000L, products.get(6), marts.get(0), null),

                // 마트 2
                new MartProduct(20L, 1450L, products.get(1), marts.get(1), null),
                new MartProduct(30L, 5000L, products.get(3), marts.get(1), discounts.get(3)),
                new MartProduct(20L, 1400L, products.get(6), marts.get(1), discounts.get(1)),

                // 마트 3
                new MartProduct(30L, 1550L, products.get(1), marts.get(2), discounts.get(1)),
                new MartProduct(20L, 4500L, products.get(3), marts.get(2), discounts.get(2)),
                new MartProduct(30L, 2000L, products.get(6), marts.get(2), null),

                // 마트 4
                new MartProduct(20L, 4500L, products.get(3), marts.get(3), discounts.get(3)),
                new MartProduct(30L, 2000L, products.get(6), marts.get(3), null),

                // 마트 5
                new MartProduct(40L, 1500L, products.get(1), marts.get(4), null),
                new MartProduct(30L, 4600L, products.get(3), marts.get(4), discounts.get(2)),
                new MartProduct(20L, 1500L, products.get(6), marts.get(4), discounts.get(0))
        );
        martProductRepository.saveAll(martProducts);

        /* 회원 카트에 담긴 상품 목록 */
        Member userA = new Member("test@test.com", "회원A");
        memberRepository.save(userA);
        Member userB = new Member("test1@test.com", "회원B");
        memberRepository.save(userB);

        // 카트에 상품 추가
        List<Cart> cartItems = Arrays.asList(
                new Cart(2L, userA, products.get(1)),
                new Cart(1L, userA, products.get(3)),
                new Cart(5L, userA, products.get(6))
        );
        cartRepository.saveAll(cartItems);
    }
}
