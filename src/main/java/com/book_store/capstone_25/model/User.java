package com.book_store.capstone_25.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import lombok.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.processing.Pattern;
import org.springframework.boot.convert.DataSizeUnit;

import java.util.ArrayList;
import java.util.List;


@Data
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                                   // PK

    @Column(name = "user_id", nullable = false,
            unique = true, length = 30)
    private String userId;                             // 로그인 ID

    @Column(nullable = false)
    private String password;

    // ────────── 기타 프로필 ──────────
    private String name;
    private Integer age;
    private String email;
    private String phoneNumber;
    private String address;
    private String birthDate;

    @Column(name = "cardnumber")
    private String cardNumber;
    @Column(name = "cardtype")
    private String cardType;
    @Column(name = "bank_account")
    private String bankAccount;

    @Enumerated(EnumType.STRING)
    private Gender gender;



    @Getter
    // UserInterest 리스트


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
        this.cardNumber = updatedUser.cardNumber;
        this.cardType = updatedUser.cardType;
        this.bankAccount = updatedUser.bankAccount;
        // 추가로 업데이트해야 할 필드가 있다면 여기에 추가하세요
    }

    public Iterable<? extends User_Interest> getUserInterests() {
        return this.user_interest;
    }

    @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
     List<User_Interest> user_interest;

}
