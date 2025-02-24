package com.book_store.capstone_25.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long id;         // CartItem의 식별자 (필요에 따라)
    private Long bookId;     // 선택한 책의 ID
    private String bookTitle;
    private BigDecimal price;
    private int quantity;
    private String imageUrl; // 책의 이미지 경로
}
