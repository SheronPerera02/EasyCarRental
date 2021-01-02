package com.easyCarRental.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DashboardDTO {
    int registeredUsers;
    int totalBookings;
    int activeBookings;
    int availableCars;
    int reservedCars;
    int carsNeedMaintenance;
    int carsUnderMaintenance;
    int availableDrivers;
    int occupiedDrivers;
}
