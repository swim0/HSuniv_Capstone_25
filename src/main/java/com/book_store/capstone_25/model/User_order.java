
package com.book_store.capstone_25.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Entity

public class User_order {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "book_name", nullable = false)
    private String bookName;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

}
