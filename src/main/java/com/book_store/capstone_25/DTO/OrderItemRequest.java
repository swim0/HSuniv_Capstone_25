package com.book_store.capstone_25.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {
    private Long bookId; // 주문할 도서 ID
    private int quantity; // 주문 수량
}

