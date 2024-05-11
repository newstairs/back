package project.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import project.back.service.CartService;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


}
