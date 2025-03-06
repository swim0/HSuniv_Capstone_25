package com.book_store.capstone_25.model;

import jakarta.persistence.*;

@lombok.Getter
@lombok.Setter
@Entity
@Table(name = "user_such")
public class User_Such {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private String authenticationCode;

    public User_Such(String token, User user, String authenticationCode) {
        this.token = token;
        this.user = user;
        this.authenticationCode = authenticationCode;
    }


    public User_Such() {
    }

}
