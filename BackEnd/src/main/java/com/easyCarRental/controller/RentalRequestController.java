package com.easyCarRental.controller;

import com.easyCarRental.dto.DriverDTO;
import com.easyCarRental.dto.RentalRequestDTO;
import com.easyCarRental.dto.Rented_CarDTO;
import com.easyCarRental.service.DriverService;
import com.easyCarRental.service.RentalRequestService;
import com.easyCarRental.service.Rented_CarService;
import com.easyCarRental.service.Rented_DriverService;
import com.easyCarRental.util.StandardResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/RentalRequest")
@CrossOrigin
public class RentalRequestController {

    @Autowired
    RentalRequestService rentalRequestService;

    @Autowired
    Rented_CarService rented_carService;

    @Autowired
    Rented_DriverService rented_driverService;

    @Autowired
    DriverService driverService;

    @PostMapping("/addRentalRequest")
    public ResponseEntity addRentalRequest(@RequestParam String rentalRequest, @RequestParam String rentedCars) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        RentalRequestDTO rentalRequestDTO = objectMapper.readValue(rentalRequest, RentalRequestDTO.class);
        List<Rented_CarDTO> rented_carDTOS = Arrays.asList(objectMapper.readValue(rentedCars, Rented_CarDTO[].class));

        int requestedDriverCount = 0;

        for (Rented_CarDTO dto : rented_carDTOS) {
            if (dto.getDriverNeeded().contentEquals("Yes")) {
                requestedDriverCount++;
            }
        }
        int availableDriversCount = driverService.getAvailableDriverCount();
        if (requestedDriverCount > availableDriversCount) {
            throw new RuntimeException(" Drivers Available : " + availableDriversCount);
        }


        rentalRequestService.addRentalRequest(rentalRequestDTO, rented_carDTOS);
        rented_carService.addRentedCars(rentalRequestDTO.getRequestId(), rented_carDTOS);
        rented_driverService.addRentedDrivers(rentalRequestDTO.getRequestId(), rented_carDTOS);

        StandardResponse response = new StandardResponse(200, "Success", null);
        return new ResponseEntity(response, HttpStatus.OK);
    }


    @GetMapping("/getNextId")
    public ResponseEntity getNextId() {
        StandardResponse response = new StandardResponse(200, "Success", rentalRequestService.getNextId());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/uploadProofImages")
    public ResponseEntity uploadProof(@RequestParam MultipartFile[] proofImages, String[] fileNames) {

        String[] extensions = new String[proofImages.length];

        for (int i = 0; i < proofImages.length; i++) {
            extensions[i] = upload(fileNames[i] + ".", proofImages[i]);
        }

        StandardResponse response = new StandardResponse(200, "Success", extensions);
        return new ResponseEntity(response, HttpStatus.OK);
    }


    private String upload(String namePrefix, MultipartFile multipartFile) {
        String extension = getExtension(multipartFile.getOriginalFilename());
        String fileName = namePrefix + extension;
        try {
            File image = new File(fileName);

            OutputStream outputStream = new FileOutputStream(image);

            outputStream.write(multipartFile.getBytes());

            BufferedImage bufferedImage = ImageIO.read(image);

            File dir = new File("/home/johash/Desktop/EasyCarRental/FrontEnd/WaiverProof");

            if (!dir.exists()) {
                dir.mkdir();
            }

            File file = new File("/home/johash/Desktop/EasyCarRental/FrontEnd/WaiverProof/" + fileName);

            ImageIO.write(bufferedImage, getExtension(multipartFile.getOriginalFilename()), file);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return extension;
    }

    public String getExtension(String fileName) {
        String extensionReversed = "";

        for (int i = fileName.length() - 1; i >= 0; i--) {
            if ((fileName.charAt(i) + "").contentEquals(".")) {
                break;
            }
            extensionReversed += fileName.charAt(i);
        }
        String extension = "";

        for (int i = extensionReversed.length() - 1; i >= 0; i--) {
            extension += extensionReversed.charAt(i);
        }
        return extension;
    }

    @GetMapping(path = "/checkRentStatus/{nic}")
    public ResponseEntity checkRentStatus(@PathVariable String nic) {

        StandardResponse standardResponse;

        String requestStatus = rentalRequestService.getRequestStatus(nic);

        if (requestStatus.contentEquals("Finalized") || requestStatus.contentEquals("Cancelled")) {
            standardResponse = new StandardResponse(200, "Success", null);
        } else if (requestStatus.contentEquals("Rejected")) {
            standardResponse = new StandardResponse(200, "Rejected", rentalRequestService.getDenialMessage(nic));
        } else if (requestStatus.contentEquals("Pending")) {
            standardResponse = new StandardResponse(200, "Pending", rentalRequestService.getReqId(nic));
        } else {
            List<DriverDTO> driverDetails = rentalRequestService.getDriverDetails(nic);
            standardResponse = new StandardResponse(200, "Active", driverDetails);
        }
        return new ResponseEntity(standardResponse, HttpStatus.OK);
    }

    @DeleteMapping(path = "/cancelRequest/{reqId}")
    public ResponseEntity cancelRequest(@PathVariable String reqId) {
        rentalRequestService.cancelRequest(reqId);
        StandardResponse response = new StandardResponse(200, "Success", null);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping(path = "/getAllRentalRequests")
    public ResponseEntity getAllRequests() {
        StandardResponse response = new StandardResponse(200, "Success", rentalRequestService.getAllRequests());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping(path = "/changeDrivers")
    public ResponseEntity changeDrivers(@RequestParam String data, @RequestParam String reqId) throws JsonProcessingException {

        String[][] drivers = new ObjectMapper().readValue(data, String[][].class);
        rentalRequestService.changeDrivers(drivers, reqId);
        StandardResponse response = new StandardResponse(200, "Success", null);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping(path = "/acceptRequest")
    public ResponseEntity acceptRequest(@RequestParam String reqId) {
        rentalRequestService.acceptRequest(reqId);
        StandardResponse response = new StandardResponse(200, "Success", null);
        return new ResponseEntity(response, HttpStatus.OK);
    }


    @PostMapping(path = "/denyRequest/{reqId}")
    public ResponseEntity denyRequest(@PathVariable String reqId, @RequestParam String message) {
        rentalRequestService.denyRequest(reqId, message);
        StandardResponse response = new StandardResponse(200, "Success", null);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping(path = "/getRequestsForPayment")
    public ResponseEntity getRequestsForFinalizingPayment() {
        StandardResponse response = new StandardResponse(200, "Success", rentalRequestService.getRequestsForFinalizingPayment());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping(path = "/getCarsForPayment/{reqId}")
    public ResponseEntity getCarsForFinalizingPayment(@PathVariable String reqId) {
        StandardResponse response = new StandardResponse(200, "Success", rentalRequestService.getCarsForFinalizingPayment(reqId));
        return new ResponseEntity(response, HttpStatus.OK);
    }


    @PostMapping (path = "/finalizePayment/{reqId}/{additional}")
    public ResponseEntity finalizePayment(@PathVariable String additional, @PathVariable String reqId, @RequestParam String details) throws JsonProcessingException {
        String[][] carDetails = new ObjectMapper().readValue(details, String[][].class);
        List<String> strings = rentalRequestService.finalizePayment(carDetails, reqId, additional);
        StandardResponse response = new StandardResponse(200, "Success", strings);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping( path = "/getIncome/{year}")
    public ResponseEntity getIncome(@PathVariable String year){
        StandardResponse response = new StandardResponse(200, "Success", rentalRequestService.getIncome(year));
        return new ResponseEntity(response, HttpStatus.OK);
    }

}
