package com.book_store.capstone_25.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryRequest {
    private String address;         // 배송 주소
    private String trackingNumber;  // 운송장 번호 (선택 사항)
}
