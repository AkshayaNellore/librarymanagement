package com.cts.lms.entity;

import java.util.Set;
import java.util.UUID;
import com.cts.lms.enums.Membership;
import com.cts.lms.enums.Role;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "memberId")
    private String memberId;

    @PrePersist
    public void generateuuid() {
        this.memberId = UUID.randomUUID().toString();
    }

    @Column(name = "name")
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "membershipStatus")
    private Membership membershipStatus;

    @Column(name = "userType")
	@CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "memberId"))
    private String userType;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password")
    private String password;
}