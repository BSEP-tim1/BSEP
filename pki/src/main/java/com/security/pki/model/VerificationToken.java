package com.security.pki.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="validation_tokens")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Date createdDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String role;

    public VerificationToken() {
        super();
    }

    public VerificationToken(User user) {
        super();
        this.token = UUID.randomUUID().toString();;
        this.createdDate = new Date();
        this.user = user;
        this.role = user.getUserType().getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreatedDateTime() {
        return createdDate;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDate = createdDateTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }
}
