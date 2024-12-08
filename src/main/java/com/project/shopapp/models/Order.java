package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "fullname", length = 100)
    private String fullname;
    @Column(name = "email", length = 100)
    private String email;
    @Column(name = "phone", length = 20)
    private String phone;
    @Column(name = "address", length = 200)
    private String address;
    @Column(name = "note", length = 100)
    private String note;
    private Date orders_date;
    private LocalDate shipping_date;
    @Column(name = "shipping_method", length = 100)
    private String shipping_method;
    @Column(name = "shipping_address", length = 200)
    private String shipping_address;
    @Column(name = "tracking_number", length = 100)
    private String tracking_number;
    @Column(name = "payment_method", length = 100)
    private String payment_method;
    @Column(name = "active")
    private Boolean active;
    @Column(name = "status")
    private String status;
}

