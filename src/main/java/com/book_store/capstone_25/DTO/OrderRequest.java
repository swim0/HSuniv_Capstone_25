package com.book_store.capstone_25.DTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Long userId; // 주문자 ID
    private List<OrderItemRequest> items; // 주문 도서 목록
    private String address;
    private String couponCode; // 쿠폰 코드 (선택 사항)
    private double totalPrice;

    @Getter
    @Setter
    public static class OrderItemRequest {
        private Long bookId; // 도서 ID
        private int quantity; // 수량
    }

}

