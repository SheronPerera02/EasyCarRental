package com.easyCarRental.service.impl;

import com.easyCarRental.dto.DriverDTO;
import com.easyCarRental.repo.DriverRepo;
import com.easyCarRental.service.DriverService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    DriverRepo driverRepo;

    @Autowired
    ModelMapper modelMapper;


    public int getAvailableDriverCount() {
        return driverRepo.getAvailableDriverCount();
    }

    public List<DriverDTO> getAllAvailableDrivers(){
        return modelMapper.map(driverRepo.getAllAvailableDrivers(),new TypeToken<List<DriverDTO>>(){}.getType());
    }
}
