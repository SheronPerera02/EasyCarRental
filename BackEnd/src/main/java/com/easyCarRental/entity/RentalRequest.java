package com.easyCarRental.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RentalRequest {
    @Id
    String requestId;
    String durationPlan;
    String pickupDate;
    String pickupTime;
    String pickupVenue;
    String returnDate;
    String returnTime;
    String returnVenue;

    String rentStatus;
    String description;

    @OneToMany(mappedBy = "rentalRequest", cascade = CascadeType.ALL)
    List<Rented_Car> rentedCarList = new ArrayList<Rented_Car>();

    @OneToMany(mappedBy = "rentalRequest", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Rented_Driver> rentedDrivers = new ArrayList<Rented_Driver>();

    @ManyToOne
    @JoinColumn(name = "userId",referencedColumnName = "nic")
    User user;

    @OneToMany(mappedBy = "rentalRequest")
    List<Payment> paymentList = new ArrayList<Payment>();

}
