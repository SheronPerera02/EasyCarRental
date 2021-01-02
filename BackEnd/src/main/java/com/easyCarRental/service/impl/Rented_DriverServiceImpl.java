package com.easyCarRental.service.impl;

import com.easyCarRental.dto.Rented_CarDTO;
import com.easyCarRental.entity.Driver;
import com.easyCarRental.entity.RentalRequest;
import com.easyCarRental.entity.Rented_Driver;
import com.easyCarRental.repo.DriverRepo;
import com.easyCarRental.repo.RentalRequestRepo;
import com.easyCarRental.repo.Rented_DriverRepo;
import com.easyCarRental.service.Rented_DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Rented_DriverServiceImpl implements Rented_DriverService {

    @Autowired
    DriverRepo driverRepo;

    @Autowired
    Rented_DriverRepo rented_driverRepo;

    @Autowired
    RentalRequestRepo rentalRequestRepo;


    public void addRentedDrivers(String requestId, List<Rented_CarDTO> rentedCarDTOS) {
        RentalRequest rentalRequest = rentalRequestRepo.findById(requestId).get();

        for (Rented_CarDTO dto : rentedCarDTOS) {
            if (dto.getDriverNeeded().contentEquals("Yes")) {
                Driver driver = getDriver();
                rented_driverRepo.save(new Rented_Driver(
                        rentalRequest.getRequestId() + driver.getId(),
                        rentalRequest,
                        driver
                ));
            }

        }
    }

    private Driver getDriver(){
        Driver driver = driverRepo.getDriver();
        driverRepo.markUnavailable(driver.getId());
        return driver;
    }

}
