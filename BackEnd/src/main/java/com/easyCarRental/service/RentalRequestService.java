package com.easyCarRental.service;

import com.easyCarRental.dto.CarDTO;
import com.easyCarRental.dto.DriverDTO;
import com.easyCarRental.dto.RentalRequestDTO;
import com.easyCarRental.dto.Rented_CarDTO;

import java.util.List;

public interface RentalRequestService {
    void addRentalRequest(RentalRequestDTO rentalRequestDTO, List<Rented_CarDTO> rentedCarDTOS);

    String getNextId();

    String getRequestStatus(String nic);

    List<DriverDTO> getDriverDetails(String nic);

    String getReqId(String nic);

    void cancelRequest(String reqId);

    List<RentalRequestDTO> getAllRequests();

    void changeDrivers(String[][] drivers, String reqId);

    void acceptRequest(String reqId);

    void denyRequest(String reqId, String message);

    String getDenialMessage(String nic);

    List<RentalRequestDTO> getRequestsForFinalizingPayment();

    List<CarDTO> getCarsForFinalizingPayment(String reqId);

    List<String> finalizePayment(String[][] carDetails, String reqId, String additional);

    List<String> getIncome(String year);

}
