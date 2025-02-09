package com.book_store.capstone_25.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "user_id")
    private String userId;
    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private String age;
    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phoneNumber;
    @Column(name = "address")
    private String address;
    @Column(name = "birthdate")

    private String birthDate;
    @Column(name = "cardnumber")
    private String cardNumber;
    @Column(name = "cardtype")
    private String cardType;

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
        this.age = updatedUser.age;
        // 추가로 업데이트해야 할 필드가 있다면 여기에 추가하세요
    }


    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
     List<User_order> user_order;


    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
     List<User_Interest> user_interest;

}
