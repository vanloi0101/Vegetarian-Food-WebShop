package com.devteria.identityservice.entity;



import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "total_amount", nullable = false)
    Double totalAmount;

    @Column(name = "status") // Đã bỏ @Enumerated
    String status;

    @Column(name = "payment_status") // Đã bỏ @Enumerated
    String paymentStatus;

    @Column(name = "shipping_address")
    String shippingAddress;

    @Column(name = "payment_method")
    String paymentMethod;


    @Column(name = "created_at")
    Date createdAt;

    @Column(name = "updated_at")
    Date updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<OrderDetail> orderDetails;
}