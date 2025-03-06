package com.book_store.capstone_25.controller;

import com.book_store.capstone_25.model.Cart;
import com.book_store.capstone_25.model.User;
import com.book_store.capstone_25.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final ShoppingCartService shoppingCartService;

    // ✅ 장바구니 도서 추가
    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestParam Long userId, @RequestParam Long bookId, @RequestParam int quantity) {
        User user = new User();
        user.setId(userId);
        return ResponseEntity.ok(shoppingCartService.addToCart(user, bookId, quantity));
    }

    // ✅ 장바구니 개별 항목 삭제
    @DeleteMapping("/remove/{bookId}")
    public ResponseEntity<Cart> removeCartItem(@RequestParam Long userId, @PathVariable Long bookId) {
        User user = new User();
        user.setId(userId);
        return ResponseEntity.ok(shoppingCartService.removeCartItem(user, bookId));
    }

    // ✅ 장바구니 전체 비우기
    @DeleteMapping("/clear")
    public ResponseEntity<Cart> clearCart(@RequestParam Long userId) {
        User user = new User();
        user.setId(userId);
        return ResponseEntity.ok(shoppingCartService.clearCart(user));
    }

    // ✅ 장바구니 수량 변경
    @PutMapping("/update/{bookId}")
    public ResponseEntity<Cart> updateCartItemQuantity(@RequestParam Long userId, @PathVariable Long bookId, @RequestParam int quantity) {
        User user = new User();
        user.setId(userId);
        return ResponseEntity.ok(shoppingCartService.updateCartItemQuantity(user, bookId, quantity));
    }
    // ✅ 장바구니 조회
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        return ResponseEntity.ok(shoppingCartService.getCartByUser(user));
    }
}
