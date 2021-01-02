package com.easyCarRental.controller;

import com.easyCarRental.dto.CarDTO;
import com.easyCarRental.service.CarService;
import com.easyCarRental.util.StandardResponse;
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
import java.util.List;

@RestController
@RequestMapping("/api/v1/Car")
@CrossOrigin
public class CarController {

    @Autowired
    CarService carService;

    @PostMapping(path = "/addCar")
    public ResponseEntity addCar(@RequestBody CarDTO carDTO) {
        carService.addCar(carDTO);
        StandardResponse response = new StandardResponse(200, "Success", null);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping(path = "/uploadImages")
    public ResponseEntity uploadImages(@RequestParam("files") MultipartFile[] multipartFile, @RequestParam String regNo) {


        if (carService.regNoAlreadyExists(regNo)) {
            throw new RuntimeException("Duplicate Registration No Entry!");
        }

        String[] aspects = {"Front.", "Back.", "Side.", "Interior."};
        String[] extensions = new String[4];
        for (int i = 0; i < multipartFile.length; i++) {
            extensions[i] = upload(regNo + aspects[i], multipartFile[i]);
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

            File dir = new File("/home/johash/Desktop/EasyCarRental/FrontEnd/CarImages");

            if (!dir.exists()) {
                dir.mkdir();
            }

            File file = new File("/home/johash/Desktop/EasyCarRental/FrontEnd/CarImages/" + fileName);

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

    @GetMapping(path = "/getAllCars")
    public ResponseEntity getAllCars() {
        List<CarDTO> allCars = carService.getAllCars();
        StandardResponse standardResponse = new StandardResponse(200, "Success", allCars);
        return new ResponseEntity(standardResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/getAllCarsForAdmin")
    public ResponseEntity getAllCarsForAdmin() {
        List<CarDTO> allCars = carService.getAllCarsForAdmin();
        StandardResponse standardResponse = new StandardResponse(200, "Success", allCars);
        return new ResponseEntity(standardResponse, HttpStatus.OK);
    }

    @PutMapping(path = "/markDamaged/{regNo}")
    public ResponseEntity markDamaged(@PathVariable String regNo) {
        carService.markDamaged(regNo);
        StandardResponse standardResponse = new StandardResponse(200, "Success", null);
        return new ResponseEntity(standardResponse, HttpStatus.OK);
    }

    @PutMapping(path = "/addToMaintenance/{regNo}")
    public ResponseEntity addToMaintenance(@PathVariable String regNo) {
        carService.addToMaintenance(regNo);
        StandardResponse standardResponse = new StandardResponse(200, "Success", null);
        return new ResponseEntity(standardResponse, HttpStatus.OK);
    }

    @PutMapping(path = "/markRepaired/{regNo}")
    public ResponseEntity markRepaired(@PathVariable String regNo) {
        carService.markRepaired(regNo);
        StandardResponse standardResponse = new StandardResponse(200, "Success", null);
        return new ResponseEntity(standardResponse, HttpStatus.OK);
    }

    @DeleteMapping(path = "/removeCar/{regNo}")
    public ResponseEntity removeCar(@PathVariable String regNo) {
        List<String> images = carService.getImages(regNo);
        carService.removeCar(regNo);
        for(String image : images){
            deleteImage(image);
        }
        StandardResponse standardResponse = new StandardResponse(200, "Success", null);
        return new ResponseEntity(standardResponse, HttpStatus.OK);
    }

    private void deleteImage(String fileName) {
        File file = new File("/home/johash/Desktop/EasyCarRental/FrontEnd/CarImages/" + fileName);
        file.delete();
    }

    @PostMapping(path = "/updateImages")
    public ResponseEntity updateImages(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam String[] updateFileNames,
            @RequestParam String[] deletableFilesNames
    ) {

        for (String fileName : deletableFilesNames) {
            deleteImage(fileName);
        }
        String[] extensions = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            extensions[i] = upload(updateFileNames[i], files[i]);
        }
        StandardResponse standardResponse = new StandardResponse(200, "Success!", extensions);

        return new ResponseEntity(standardResponse, HttpStatus.OK);
    }

    @PutMapping(path = "/updateCar")
    public ResponseEntity updateCar(@RequestBody CarDTO carDTO) {
        carService.updateCar(carDTO);

        StandardResponse standardResponse = new StandardResponse(200, "Success!", null);
        return new ResponseEntity(standardResponse, HttpStatus.OK);
    }


}
