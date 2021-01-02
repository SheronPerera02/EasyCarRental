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
public class Car {
    @Id
    private String registrationNo;
    private String brand;
    private String type;
    private String frontImage;
    private String backImage;
    private String sideImage;
    private String interiorImage;
    private int numberOfPassengers;
    private String transmissionType;
    private String fuelType;
    private String color;
    private double dailyRate;
    private double monthlyRate;
    private int freeMileagePerDay;
    private int freeMileagePerMonth;
    private double pricePerKm;
    private int kmMeterValue;
    private String lastReturnDate;
    private String isAvailable;
    private String isDamaged;
    private String underMaintenance;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Rented_Car> rentedCarList = new ArrayList<Rented_Car>();

}
