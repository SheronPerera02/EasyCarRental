package com.easyCarRental.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Payment {

    @Id
    String paymentId;
    String description;

    @ManyToOne
    @JoinColumn(name = "reqId", referencedColumnName = "requestId")
    RentalRequest rentalRequest;

    double amount;

}
