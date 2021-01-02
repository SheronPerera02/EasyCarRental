package com.easyCarRental.dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RentalRequestDTO {

    String requestId;
    String durationPlan;
    String userId;
    String pickupDate;
    String pickupTime;
    String pickupVenue;
    String returnDate;
    String returnTime;
    String returnVenue;
    String rentStatus;
    String description;

    List<String> driverIds;
    List<String> driverNames;
    List<String> carRegNumbers;
}
