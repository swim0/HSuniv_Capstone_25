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

    private final ShoppingCartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;

    public ShoppingCartService(ShoppingCartRepository cartRepository,
                               CartItemRepository cartItemRepository,
                               BookRepository bookRepository, OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
    }

    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    // ✅ 장바구니에 도서 추가 (수량 조정)
    public Cart addToCart(User user, Long bookId, int quantity) {
        Cart cart = getCartByUser(user);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Optional<CartItem> optionalItem = cart.getItems().stream()
                .filter(item -> item.getBook().getBookId().equals(bookId))
                .findFirst();

        if (optionalItem.isPresent()) {
            CartItem item = optionalItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setBook(book);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }
        return cartRepository.save(cart);
    }

    // ✅ 장바구니에서 특정 도서 삭제
    public Cart removeCartItem(User user, Long bookId) {
        Cart cart = getCartByUser(user);
        cart.getItems().removeIf(item -> item.getBook().getBookId().equals(bookId));
        return cartRepository.save(cart);
    }

    // ✅ 장바구니 비우기 (전체 삭제)
    public Cart clearCart(User user) {
        Cart cart = getCartByUser(user);
        cart.getItems().clear();
        return cartRepository.save(cart);
    }

    // ✅ 장바구니에서 특정 도서 수량 변경 (업데이트)
    public Cart updateCartItemQuantity(User user, Long bookId, int quantity) {
        Cart cart = getCartByUser(user);
        Optional<CartItem> optionalItem = cart.getItems().stream()
                .filter(item -> item.getBook().getBookId().equals(bookId))
                .findFirst();

        if (optionalItem.isPresent()) {
            CartItem item = optionalItem.get();
            if (quantity <= 0) {
                cart.getItems().remove(item); // 수량이 0 이하일 경우 삭제
            } else {
                item.setQuantity(quantity);
            }
            return cartRepository.save(cart);
        } else {
            throw new RuntimeException("CartItem not found");
        }
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
        order.setStatus("결제 이전");

        double totalAmount = 0;
        List<Order.OrderItemDetails> orderItemDetailsList = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {
            BigDecimal price = cartItem.getBook().getPrice();
            int quantity = cartItem.getQuantity();
            totalAmount += price.doubleValue() * quantity;

            Order.OrderItemDetails details = new Order.OrderItemDetails();
            details.setBookId(cartItem.getBook().getBookId());
            details.setBookTitle(cartItem.getBook().getTitle());
            details.setQuantity(quantity);
            details.setPrice(price);
            orderItemDetailsList.add(details);
        }
        order.setOrderItems(orderItemDetailsList);
        order.setTotalAmount(totalAmount);
        order.setDiscountedAmount(totalAmount);

        orderRepository.save(order);

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
