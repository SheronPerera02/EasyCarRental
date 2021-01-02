package com.easyCarRental.service;

import com.easyCarRental.dto.Rented_CarDTO;

import java.util.List;

public interface Rented_DriverService {

    void addRentedDrivers(String requestId, List<Rented_CarDTO> rentedCarDTOS);

}
