package com.darkness.userService.domain;

import com.darkness.commons.db.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * @author darkness
 **/
@Entity
@Table(name = "user")
public class User extends BaseEntity {
    @Column(name = "user_id")
    private String userId;
    @Column(name = "email")
    private String email;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "identification_fk", referencedColumnName = "pk")
    private Identification identification;
    @Column(name = "password")
    private String password;
    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;
    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "user_role")
    private UserRoleEnum userRole;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Identification getIdentification() {
        return identification;
    }

    public void setIdentification(Identification identification) {
        this.identification = identification;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public UserRoleEnum getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRoleEnum userRole) {
        this.userRole = userRole;
    }
}
