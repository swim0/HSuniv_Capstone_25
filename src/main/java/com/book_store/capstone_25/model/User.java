package com.book_store.capstone_25.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

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
    private Gender gender;
    public enum Gender {
        M, F
    }

    public void updateUser(User updatedUser) {
        this.name = updatedUser.name;
        this.email = updatedUser.email;
        this.phoneNumber = updatedUser.phoneNumber;
        this.address = updatedUser.address;
        this.birthDate = updatedUser.birthDate;
        // 추가로 업데이트해야 할 필드가 있다면 여기에 추가하세요
    }


    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
     List<User_order> user_order;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
     List<Book> Book;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
     List<UserInterest> Userinterest;

}
