package com.book_store.capstone_25.service;

import com.book_store.capstone_25.DTO.CartItemDTO;
import com.book_store.capstone_25.DTO.ShoppingCartDTO;
import com.book_store.capstone_25.Repository.BookRepository;
import com.book_store.capstone_25.Repository.CartItemRepository;
import com.book_store.capstone_25.Repository.OrderRepository;
import com.book_store.capstone_25.Repository.ShoppingCartRepository;
import com.book_store.capstone_25.model.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ShoppingCartService {

    // 필요한 Repository (ShoppingCartRepository, CartItemRepository, BookRepository 등)
    private final ShoppingCartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;

    // 생성자 주입
    public ShoppingCartService(ShoppingCartRepository cartRepository,
                               CartItemRepository cartItemRepository,
                               BookRepository bookRepository, OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
    }

    public Cart getCartByUser(User user) {
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        return cart;
    }

    public Cart addToCart(User user, Long bookId, int quantity) {
        Cart cart = getCartByUser(user);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // 기존에 장바구니에 같은 책이 있는지 확인
        Optional<CartItem> optionalItem = cart.getItems().stream()
                .filter(item -> item.getBook().getBookId().equals(bookId))
                .findFirst();

        if (optionalItem.isPresent()) {
            // 수량 업데이트
            CartItem item = optionalItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            // 새 항목 추가
            CartItem newItem = new CartItem();
            newItem.setBook(book);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }
        return cartRepository.save(cart);
    }

    public Cart removeFromCart(User user, Long bookId) {
        Cart cart = getCartByUser(user);
        cart.getItems().removeIf(item -> item.getBook().getBookId().equals(bookId));
        return cartRepository.save(cart);
    }

    @Transactional
    public Order checkout(User user) {
        Cart cart = getCartByUser(user);
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING"); // 기본 주문 상태 설정

        // 주문 상세 항목 생성 및 총 금액 계산
        double totalAmount = 0;
        List<Order.OrderItemDetails> orderItemDetailsList = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {
            BigDecimal price = cartItem.getBook().getPrice();
            int quantity = cartItem.getQuantity();
            totalAmount += price.doubleValue() * quantity;

            Order.OrderItemDetails details = new Order.OrderItemDetails();
            details.setBookTitle(cartItem.getBook().getTitle());
            details.setQuantity(quantity);
            details.setPrice(price);
            orderItemDetailsList.add(details);
        }
        order.setOrderItems(orderItemDetailsList);
        order.setTotalAmount(totalAmount);
        order.setDiscountedAmount(totalAmount); // 할인 로직이 있다면 적용

        // Payment, Delivery, Coupon 등은 필요에 따라 설정 또는 나중에 처리

        // Order 저장 (OrderRepository 필요)
        orderRepository.save(order);

        // 장바구니 비우기
        cart.getItems().clear();
        cartRepository.save(cart);

        return order;
    }

    public ShoppingCartDTO convertToDTO(Cart cart) {
        List<CartItemDTO> itemDTOs = cart.getItems().stream().map(cartItem -> {
            CartItemDTO dto = new CartItemDTO();
            dto.setId(cartItem.getId());
            dto.setBookId(cartItem.getBook().getBookId());
            dto.setBookTitle(cartItem.getBook().getTitle());
            dto.setPrice(cartItem.getBook().getPrice());
            dto.setQuantity(cartItem.getQuantity());
            dto.setImageUrl(cartItem.getBook().getImageUrl());
            return dto;
        }).collect(Collectors.toList());

        ShoppingCartDTO cartDTO = new ShoppingCartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setItems(itemDTOs);

        return cartDTO;
    }
}
