package com.book_store.capstone_25.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    // OneToMany relationship with Order
    @Id
    @Column(name = "user_id")
    private String userId;
    @Column(name = "password")
    private String password;

    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phoneNumber;

    private String address;
    @Column(name = "birthdate")
    private String birthDate;


    @Enumerated(EnumType.STRING)

    public void updateUser(User updatedUser) {
        this.name = updatedUser.name;
        this.email = updatedUser.email;
        this.phoneNumber = updatedUser.phoneNumber;
        this.address = updatedUser.address;
        this.birthDate = updatedUser.birthDate;
        // 추가로 업데이트해야 할 필드가 있다면 여기에 추가하세요
    }
    public enum Gender {
        M, F
    }



    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User_order> userorders;
}
