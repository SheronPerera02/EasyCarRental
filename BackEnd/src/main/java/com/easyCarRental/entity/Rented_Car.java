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
public class Rented_Car {

    @Id
    String id;
    String waiverAmount;
    String waiverProof;

    @ManyToOne
    @JoinColumn(name = "reqId", referencedColumnName = "requestId")
    RentalRequest rentalRequest;

    @ManyToOne
    @JoinColumn(name = "carRegNo", referencedColumnName = "registrationNo")
    Car car;

}
