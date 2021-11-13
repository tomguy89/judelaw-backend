package com.guava.judelaw.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = {"userId"})})
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userName;

    @Column
    private String licenseNo;

    @Column
    private String email;

    @Column
    private String mobile;
}
