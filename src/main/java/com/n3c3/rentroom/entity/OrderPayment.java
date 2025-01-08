package com.n3c3.rentroom.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OrderPayment extends AbstractAudittingEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount_payment")
    private Integer amountPayment;

    @Enumerated(EnumType.STRING)
    private StatusPayment statusPayment;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmountPayment() {
        return amountPayment;
    }

    public void setAmountPayment(Integer amountPayment) {
        this.amountPayment = amountPayment;
    }

    public StatusPayment getStatusPayment() {
        return statusPayment;
    }

    public void setStatusPayment(StatusPayment statusPayment) {
        this.statusPayment = statusPayment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
