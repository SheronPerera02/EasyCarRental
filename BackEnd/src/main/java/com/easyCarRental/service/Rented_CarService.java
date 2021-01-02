package com.easyCarRental.service;

import com.easyCarRental.dto.Rented_CarDTO;

import java.util.List;

public interface Rented_CarService {
    void addRentedCars(String requestId, List<Rented_CarDTO> rentedCarDTOS);
}
