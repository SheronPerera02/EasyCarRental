package com.easyCarRental.service;

import com.easyCarRental.dto.CarDTO;

import java.util.List;

public interface CarService {
    void addCar(CarDTO carDTO);
    List<CarDTO> getAllCars();
    List<CarDTO> getAllCarsForAdmin();
    void markDamaged(String regNo);
    void addToMaintenance(String regNo);
    void markRepaired(String regNo);
    void removeCar(String regNo);
    boolean regNoAlreadyExists(String regNo);
    void updateCar(CarDTO carDTO);
    List<String> getImages(String regNo);
}
