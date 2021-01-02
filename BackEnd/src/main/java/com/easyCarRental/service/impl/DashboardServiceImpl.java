package com.easyCarRental.service.impl;

import com.easyCarRental.dto.DashboardDTO;
import com.easyCarRental.repo.CarRepo;
import com.easyCarRental.repo.DriverRepo;
import com.easyCarRental.repo.RentalRequestRepo;
import com.easyCarRental.repo.UserRepo;
import com.easyCarRental.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    RentalRequestRepo rentalRequestRepo;

    @Autowired
    CarRepo carRepo;

    @Autowired
    DriverRepo driverRepo;

    public DashboardDTO getDashboardDetails(){
        DashboardDTO dto = new DashboardDTO(
                userRepo.getUserCount(),
                rentalRequestRepo.getTotalBookings(),
                rentalRequestRepo.getActiveBookings(),
                carRepo.getAvailableCarCount(),
                carRepo.getReservedCarCount(),
                carRepo.getDamagedCarCount(),
                carRepo.getUnderMaintenanceCarCount(),
                driverRepo.getAvailableDriverCount(),
                driverRepo.getOccupiedDriverCount()
        );

        return dto;
    }

}
