package com.easyCarRental.service.impl;

import com.easyCarRental.dto.Rented_CarDTO;
import com.easyCarRental.entity.Car;
import com.easyCarRental.entity.RentalRequest;
import com.easyCarRental.entity.Rented_Car;
import com.easyCarRental.repo.CarRepo;
import com.easyCarRental.repo.RentalRequestRepo;
import com.easyCarRental.repo.Rented_CarRepo;
import com.easyCarRental.service.Rented_CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Rented_CarServiceImpl implements Rented_CarService {

    @Autowired
    Rented_CarRepo rented_carRepo;

    @Autowired
    CarRepo carRepo;

    @Autowired
    RentalRequestRepo rentalRequestRepo;

    public void addRentedCars(String requestId, List<Rented_CarDTO> rentedCarDTOS) {

        RentalRequest rentalRequest = rentalRequestRepo.findById(requestId).get();

        for (Rented_CarDTO dto : rentedCarDTOS) {
            Car car = carRepo.findById(dto.getCarRegistrationNo()).get();
            Rented_Car rented_car = new Rented_Car(
                    dto.getId(),
                    dto.getWaiverAmount(),
                    dto.getWaiverProof(),
                    rentalRequest,
                    car
            );
            rented_carRepo.save(rented_car);
        }


    }

}
