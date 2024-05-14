package project.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.back.repository.CartRepository;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

}
