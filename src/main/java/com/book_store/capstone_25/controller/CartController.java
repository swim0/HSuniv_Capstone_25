package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.DTO.ShoppingCartDTO;
import com.book_store.capstone_25.Repository.UserRepository;
import com.book_store.capstone_25.model.Cart;
import com.book_store.capstone_25.model.Order;
import com.book_store.capstone_25.model.User;
import com.book_store.capstone_25.service.ShoppingCartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart") // 장바구니 관련 API 전체 경로
public class CartController {

    private final ShoppingCartService cartService;
    private final UserRepository  userRepository;


    public CartController(ShoppingCartService cartService, UserRepository userRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    // 장바구니에 책 추가
    @PostMapping("/add")
    public ResponseEntity<ShoppingCartDTO> addToCart(
            @RequestParam Long userId,
            @RequestParam Long bookId,
            @RequestParam int quantity) {
        User user = userRepository.findUsersById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartService.addToCart(user, bookId, quantity);
        ShoppingCartDTO dto = cartService.convertToDTO(cart);
        return ResponseEntity.ok(dto);
    }


    // 장바구니에서 책 제거
    @DeleteMapping("/remove")
    public ResponseEntity<Cart> removeFromCart(@RequestParam Long userId,@RequestParam Long bookId) {
        User user = userRepository.findUsersById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartService.removeFromCart(user, bookId);
        return ResponseEntity.ok(cart);
    }

    // 장바구니 조회: 엔티티 대신 DTO 반환
    @GetMapping("/cart_read")
    public ResponseEntity<ShoppingCartDTO> viewCart(@RequestParam Long userId) {
        User user = userRepository.findUsersById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartService.getCartByUser(user);
        ShoppingCartDTO cartDTO = cartService.convertToDTO(cart);
        return ResponseEntity.ok(cartDTO);
    }

    // 장바구니에 담긴 모든 책 구매 (결제)
    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout(@RequestParam Long userId) {
        User user = userRepository.findUsersById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Order order = cartService.checkout(user);
        return ResponseEntity.ok(order);
    }
}

