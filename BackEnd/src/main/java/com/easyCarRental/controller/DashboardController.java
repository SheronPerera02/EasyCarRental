package com.easyCarRental.controller;

import com.easyCarRental.dto.DashboardDTO;
import com.easyCarRental.service.DashboardService;
import com.easyCarRental.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/Dashboard")
@CrossOrigin
public class DashboardController {

    @Autowired
    DashboardService dashboardService;

    @GetMapping("/dashboardDetails")
    public ResponseEntity getDashboardDetails() {
        DashboardDTO dashboardDetails = dashboardService.getDashboardDetails();
        StandardResponse standardResponse = new StandardResponse(200, "Sucess", dashboardDetails);
        return new ResponseEntity(standardResponse, HttpStatus.OK);
    }

}
