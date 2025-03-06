package com.book_store.capstone_25.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order; // 주문과 연결

    private Long bookId;
    private String bookTitle;
    private int quantity;
    private BigDecimal price;

    public OrderItemDetails(Order order, Long bookId, String bookTitle, int quantity, BigDecimal price) {
        this.order = order;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.price = price;
    }
}
