package com.devteria.identityservice.entity;

import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Hoặc UUID tùy theo cách bạn dùng
    Long id;

    String name;
    String description;

    @ManyToMany
    @JoinTable(
            name = "role_permissions", // Tên bảng trung gian
            joinColumns = @JoinColumn(name = "role_id"), // Khóa ngoại của bảng Role
            inverseJoinColumns = @JoinColumn(name = "permission_id") // Khóa ngoại của bảng Permission
    )
    Set<Permission> permissions;  // Quan hệ với bảng Permission
}


