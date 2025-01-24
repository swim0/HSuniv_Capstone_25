package com.book_store.capstone_25.DTO;


import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class LoginRequest {
    private String userId;
    private String password;

}