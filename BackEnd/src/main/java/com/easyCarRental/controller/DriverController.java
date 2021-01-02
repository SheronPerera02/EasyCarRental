package com.easyCarRental.controller;

import com.easyCarRental.service.DriverService;
import com.easyCarRental.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/Driver")
@CrossOrigin
public class DriverController {

    @Autowired
    DriverService driverService;

    @GetMapping(path = "/getAvailableDrivers")
    public ResponseEntity getAllAvailableDrivers() {
        StandardResponse standardResponse = new StandardResponse(200, "Success", driverService.getAllAvailableDrivers());
        return new ResponseEntity(standardResponse, HttpStatus.OK);
    }

}
