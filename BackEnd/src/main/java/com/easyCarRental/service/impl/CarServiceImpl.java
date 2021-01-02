package com.easyCarRental.service.impl;

import com.easyCarRental.dto.CarDTO;
import com.easyCarRental.entity.Car;
import com.easyCarRental.repo.CarRepo;
import com.easyCarRental.service.CarService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.HOURS;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    CarRepo carRepo;

    @Autowired
    ModelMapper modelMapper;

    public void addCar(CarDTO carDTO) {
        Car car = modelMapper.map(carDTO, Car.class);
        carRepo.save(car);
    }

    public List<CarDTO> getAllCars() {
        List<Car> allCars = carRepo.findAll();
        List<CarDTO> list = new ArrayList<CarDTO>();
        for (Car car : allCars) {
            if (car.getIsAvailable().contentEquals("Yes") && car.getLastReturnDate().contentEquals("N/A") || aDayHasPassedSinceLastReturn(car.getLastReturnDate())) {
                list.add(modelMapper.map(car, CarDTO.class));
            }
        }
        return list;
    }

    public List<CarDTO> getAllCarsForAdmin() {
        List<Car> allCars = carRepo.findAll();
        return modelMapper.map(allCars, new TypeToken<List<CarDTO>>() {
        }.getType());
    }

    private boolean aDayHasPassedSinceLastReturn(String ld) {
        long hours = HOURS.between(LocalDateTime.now(), LocalDateTime.parse(ld));
        return hours >= 24;
    }


    public void markDamaged(String regNo) {
        carRepo.markDamaged(regNo);
    }

    public void addToMaintenance(String regNo) {
        String availability = carRepo.getAvailability(regNo);
        if (availability.contentEquals("Yes")) {
            carRepo.addToMaintenance(regNo);
        } else {
            throw new RuntimeException("Cannot add an unavailable Car to Maintenance!");
        }
    }

    public void markRepaired(String regNo) {
        carRepo.markRepaired(regNo);
    }

    public void removeCar(String regNo) {
        if (carRepo.existsById(regNo)) {
            carRepo.deleteById(regNo);
        }
    }

    public boolean regNoAlreadyExists(String regNo) {
        return carRepo.existsById(regNo);
    }

    public void updateCar(CarDTO carDTO) {
        carRepo.updateCar(
                carDTO.getRegistrationNo(), carDTO.getColor(), carDTO.getDailyRate(),
                carDTO.getMonthlyRate(), carDTO.getFreeMileagePerDay(), carDTO.getPricePerKm(),
                carDTO.getFrontImage(), carDTO.getBackImage(), carDTO.getSideImage(),
                carDTO.getInteriorImage(), carDTO.getFreeMileagePerMonth()
        );
    }

    public List<String> getImages(String regNo) {
        Optional<Car> optional = carRepo.findById(regNo);
        Car car = optional.get();
        List<String> images = new ArrayList<String>();
        images.add(car.getFrontImage());
        images.add(car.getBackImage());
        images.add(car.getSideImage());
        images.add(car.getInteriorImage());
        return images;
    }

}
