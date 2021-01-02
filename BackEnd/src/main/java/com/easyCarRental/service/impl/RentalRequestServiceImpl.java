package com.easyCarRental.service.impl;

import com.easyCarRental.dto.CarDTO;
import com.easyCarRental.dto.DriverDTO;
import com.easyCarRental.dto.RentalRequestDTO;
import com.easyCarRental.dto.Rented_CarDTO;
import com.easyCarRental.entity.*;
import com.easyCarRental.repo.*;
import com.easyCarRental.service.RentalRequestService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class RentalRequestServiceImpl implements RentalRequestService {

    @Autowired
    RentalRequestRepo rentalRequestRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    DriverRepo driverRepo;

    @Autowired
    CarRepo carRepo;

    @Autowired
    PaymentRepo paymentRepo;

    public void addRentalRequest(RentalRequestDTO rentalRequestDTO, List<Rented_CarDTO> rentedCarDTOS) {
        RentalRequest rentalRequest = modelMapper.map(rentalRequestDTO, RentalRequest.class);

        User user = userRepo.findById(rentalRequestDTO.getUserId()).get();
        rentalRequest.setUser(user);

        rentalRequestRepo.save(rentalRequest);

        for (Rented_CarDTO dto : rentedCarDTOS) {
            carRepo.markUnavailable(dto.getCarRegistrationNo());
        }

    }

    public String getNextId() {
        String lastId = rentalRequestRepo.getLastId();
        return lastId == null ? "RR1" : "RR" + (Integer.parseInt(lastId.split("RR")[1]) + 1);
    }

    public String getRequestStatus(String nic) {
        return rentalRequestRepo.getRequestStatus(nic);
    }

    public List<DriverDTO> getDriverDetails(String nic) {
        String requestId = rentalRequestRepo.getRequestId(nic);
        List<Driver> driverDetails = driverRepo.getDriverDetails(requestId);
        return modelMapper.map(driverDetails, new TypeToken<List<DriverDTO>>() {
        }.getType());
    }

    public String getReqId(String nic) {
        return rentalRequestRepo.getRequestId(nic);
    }

    public void cancelRequest(String reqId) {
        rentalRequestRepo.setAsCancelled(reqId);
        rentalRequestRepo.makeDriversAvailable(reqId);
        rentalRequestRepo.makeCarsAvailable(reqId);
    }

    public List<RentalRequestDTO> getAllRequests() {
        List<RentalRequestDTO> list = modelMapper.map(rentalRequestRepo.getAllRentalRequests(), new TypeToken<List<RentalRequestDTO>>() {
        }.getType());
        for (RentalRequestDTO rentalRequestDTO : list) {
            String requestId = rentalRequestDTO.getRequestId();
            List<Car> requestedCars = carRepo.getRequestedCars(requestId);
            List<String> carList = new ArrayList<String>();
            for (Car car : requestedCars) {
                carList.add(car.getRegistrationNo());
            }
            rentalRequestDTO.setCarRegNumbers(carList);
            List<Driver> rentedDrivers = driverRepo.getRentedDrivers(requestId);
            List<String> driverIdList = new ArrayList<String>();
            List<String> driverNameList = new ArrayList<String>();
            for (Driver driver : rentedDrivers) {
                driverIdList.add(driver.getId());
                driverNameList.add(driver.getName());
            }
            rentalRequestDTO.setDriverIds(driverIdList);
            rentalRequestDTO.setDriverNames(driverNameList);
        }
        return list;
    }

    public void changeDrivers(String[][] drivers, String reqId) {
        for (int i = 0; i < drivers[0].length; i++) {
            rentalRequestRepo.changeDriver(reqId, drivers[0][i], drivers[1][i]);
            driverRepo.markAvailable(drivers[0][i]);
            driverRepo.markUnavailable(drivers[1][i]);
        }
    }

    public void acceptRequest(String reqId) {
        rentalRequestRepo.acceptRequest(reqId);
    }

    public void denyRequest(String reqId, String message) {
        rentalRequestRepo.denyRequest(reqId, message);
        rentalRequestRepo.makeDriversAvailable(reqId);
        rentalRequestRepo.makeCarsAvailable(reqId);
    }

    public String getDenialMessage(String nic) {
        return rentalRequestRepo.getDenialMessage(nic);
    }

    public List<RentalRequestDTO> getRequestsForFinalizingPayment() {
        List<RentalRequest> requestsForFinalizingPayment = rentalRequestRepo.getRequestsForFinalizingPayment();
        List<RentalRequestDTO> list = modelMapper.map(requestsForFinalizingPayment, new TypeToken<List<RentalRequestDTO>>() {
        }.getType());

        for (RentalRequestDTO dto : list) {
            List<Driver> driverDetails = driverRepo.getDriverDetails(dto.getRequestId());
            List<String> driverIds = new ArrayList<String>();
            List<String> driverNames = new ArrayList<String>();
            for (Driver driver : driverDetails) {
                driverIds.add(driver.getId());
                driverNames.add(driver.getName());
            }
            dto.setDriverIds(driverIds);
            dto.setDriverNames(driverNames);
        }
        return list;
    }

    public List<CarDTO> getCarsForFinalizingPayment(String reqId) {
        return modelMapper.map(carRepo.getRequestedCars(reqId), new TypeToken<List<CarDTO>>() {
        }.getType());
    }

    public List<String> finalizePayment(String[][] carDetails, String reqId, String additional) {
        double waiverBalance = 0;
        double waiverCost = 0;
        double rentalFee = 0;

        String pickupDate = rentalRequestRepo.getPickupDate(reqId);
        String returnDate = rentalRequestRepo.getReturnDate(reqId);

        for (String[] carDetail : carDetails) {
            String carRegNo = carDetail[0];
            waiverBalance += getWaiverPaymentAmount(carRepo.getCarType(carRegNo));
            waiverBalance -= Double.parseDouble(carDetail[1]);
            waiverCost += Double.parseDouble(carDetail[1]);
            //=================================================================
            rentalFee += getCostForRentDuration(reqId, carRegNo);
            rentalFee += getCostForTravelMileage(carRegNo, carDetail[2], reqId);
        }
        rentalFee += (getNumberOfDriversHired(reqId) * 1000) * getDateDifference(pickupDate, returnDate);

        rentalFee += Double.parseDouble(additional);
        finalization(reqId, waiverCost, rentalFee);

        List<String> list = new ArrayList<String>();
        list.add("Total Waiver Cost : " + (waiverBalance + waiverCost));
        list.add("Waiver Deduction : " + waiverCost);
        list.add("Return Waiver Balance : " + waiverBalance);
        list.add("Charge Rental Fee : " + rentalFee);
        return list;
    }

    private double getCostForRentDuration(String reqId, String carRegNo) {
        String durationPlan = rentalRequestRepo.getDurationPlan(reqId);
        String pickupDate = rentalRequestRepo.getPickupDate(reqId);
        String returnDate = rentalRequestRepo.getReturnDate(reqId);
        double rateForTheDurationPlan =
                durationPlan.contentEquals("Daily") ? carRepo.getDailyRate(carRegNo) : carRepo.getMonthlyRate(carRegNo);

        if (durationPlan.contentEquals("Daily")) {
            return (rateForTheDurationPlan * getDateDifference(pickupDate, returnDate));
        } else {
            return (rateForTheDurationPlan * (Double.parseDouble(getDateDifference(pickupDate, returnDate) + "") / 30));
        }
    }

    private double getWaiverPaymentAmount(String carType) {
        return carType.contentEquals("Luxury") ? 20000 : carType.contentEquals("Premium") ? 15000 : 10000;
    }

    private int getDateDifference(String pickupDate, String returnDate) {
        LocalDate pickupD = LocalDate.parse(pickupDate);
        LocalDate returnD = LocalDate.parse(returnDate);
        return Integer.parseInt(String.valueOf(DAYS.between(pickupD, returnD)));
    }

    private double getCostForTravelMileage(String carRegNo, String kmMeterValue, String reqId) {
        String durationPlan = rentalRequestRepo.getDurationPlan(reqId);
        String pickupDate = rentalRequestRepo.getPickupDate(reqId);
        String returnDate = rentalRequestRepo.getReturnDate(reqId);
        int exceededKms = Integer.parseInt(kmMeterValue);
        exceededKms -= carRepo.getKmMeterValue(carRegNo);

        setNewKmMeterValue(carRegNo, kmMeterValue);


        if (durationPlan.contentEquals("Daily")) {
            int freeMileage = carRepo.getFreeMileagePerDay(carRegNo) * getDateDifference(pickupDate, returnDate);
            if ((exceededKms - freeMileage) < 1) {
                return 0;
            } else {
                exceededKms -= freeMileage;
                return exceededKms * carRepo.getPricePerKm(carRegNo);
            }
        } else {
            int freeMileage =
                    (int) (carRepo.getFreeMileagePerMonth(carRegNo) * (Double.parseDouble(getDateDifference(pickupDate, returnDate) + "") / 30));
            if ((exceededKms - freeMileage) < 1) {
                return 0;
            } else {
                exceededKms -= freeMileage;
                return exceededKms * carRepo.getPricePerKm(carRegNo);
            }
        }
    }

    private void setNewKmMeterValue(String carRegNo, String kmMeterValue) {
        carRepo.setNewKmMeterValue(carRegNo, Integer.parseInt(kmMeterValue));
    }

    private int getNumberOfDriversHired(String reqId) {
        return driverRepo.getRentedDrivers(reqId).size();
    }

    private void finalization(String reqId, double waiverCost, double rentalFee) {

        rentalRequestRepo.makeDriversAvailable(reqId);
        rentalRequestRepo.makeCarsAvailable(reqId);
        rentalRequestRepo.setAsFinalized(reqId);
        rentalRequestRepo.setLastReturnDate(reqId, LocalDateTime.now().toString());

        paymentRepo.save(new Payment(
                "P" + reqId + "A1",
                "Rental Fee",
                rentalRequestRepo.findById(reqId).get(),
                rentalFee
        ));
        if (waiverCost != 0) {
            paymentRepo.save(new Payment(
                    "P" + reqId + "A2",
                    "Waiver Charge",
                    rentalRequestRepo.findById(reqId).get(),
                    waiverCost
            ));
        }
    }

    public List<String> getIncome(String year) {
        double income = rentalRequestRepo.getIncome(year);
        List<String> list = new ArrayList<String>();
        double incomeOfADay = income / 365;
        list.add(String.valueOf((int) incomeOfADay));
        list.add(String.valueOf((int) (incomeOfADay * 7)));
        list.add(String.valueOf((int) (incomeOfADay * 7 * 4)));
        list.add(String.valueOf((int) income));
        return list;
    }
}
