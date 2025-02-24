package com.book_store.capstone_25.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 장바구니에 속하는지
    @ManyToOne
    private Cart cart;

    // 어떤 책인지
    @ManyToOne
    private Book book;

    // 해당 책의 수량
    private int quantity;

    // getter, setter
}
