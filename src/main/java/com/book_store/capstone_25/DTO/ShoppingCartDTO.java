package com.book_store.capstone_25.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDTO {
    private Long id;                 // ShoppingCart의 식별자
    private List<CartItemDTO> items; // 장바구니 항목 목록
}
