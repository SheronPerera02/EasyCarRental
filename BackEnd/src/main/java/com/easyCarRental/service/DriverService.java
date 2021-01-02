package com.easyCarRental.service;

import com.easyCarRental.dto.DriverDTO;

import java.util.List;

public interface DriverService {
    int getAvailableDriverCount();

    List<DriverDTO> getAllAvailableDrivers();

}
