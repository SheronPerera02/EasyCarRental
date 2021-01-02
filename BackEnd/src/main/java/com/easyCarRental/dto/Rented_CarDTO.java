package com.easyCarRental.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Rented_CarDTO {
    String id;
    String carRegistrationNo;
    String rentalReqId;
    String waiverAmount;
    String waiverProof;
    String driverNeeded;
}
