package com.easyCarRental.controller;

import com.easyCarRental.dto.UserDTO;
import com.easyCarRental.service.UserService;
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
@RequestMapping("/api/v1/User")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(path = "/registerUser")
    public ResponseEntity registerUser(@RequestBody UserDTO userDTO) {
        userService.registerUser(userDTO);
        StandardResponse standardResponse = new StandardResponse(200, "Success!", null);
        return new ResponseEntity(standardResponse, HttpStatus.OK);
    }

    @PostMapping(path = "/uploadIdImage")
    public ResponseEntity uploadIdImage(@RequestParam("file") MultipartFile multipartFile, @RequestParam String nic) {

        if (userService.nicAlreadyExists(nic)) {
            throw new RuntimeException("Duplicate NIC Entry!");
        }

        StandardResponse standardResponse = new StandardResponse(200, "Success!", upload(nic, multipartFile));
        return new ResponseEntity(standardResponse, HttpStatus.OK);
    }

    private String upload(String nic, MultipartFile multipartFile) {
        String extension = new CarController().getExtension(multipartFile.getOriginalFilename());
        String fileName = nic + "." + extension;
        try {
            File image = new File(fileName);

            OutputStream outputStream = new FileOutputStream(image);

            outputStream.write(multipartFile.getBytes());

            BufferedImage bufferedImage = ImageIO.read(image);

            File dir = new File("/home/johash/Desktop/EasyCarRental/FrontEnd/IdImages");

            if (!dir.exists()) {
                dir.mkdir();
            }

            File file = new File("/home/johash/Desktop/EasyCarRental/FrontEnd/IdImages/" + fileName);

            ImageIO.write(bufferedImage, extension, file);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return extension;
    }

    @GetMapping(path = "/getAllUsers")
    public ResponseEntity getAllUsers() {
        List<UserDTO> allUsers = userService.getAllUsers();
        StandardResponse standardResponse = new StandardResponse(200, "Success!", allUsers);
        return new ResponseEntity(standardResponse, HttpStatus.OK);
    }


    @GetMapping(path = "/getIdPhoto/{nic}")
    public ResponseEntity getIdPhoto(@PathVariable String nic) {
        String idPhoto = userService.getIdPhoto(nic);
        StandardResponse standardResponse = new StandardResponse(200, "Success!", idPhoto);
        return new ResponseEntity(standardResponse, HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    public ResponseEntity login(@RequestParam String nic, @RequestParam String password) {
        if (!userService.accountExists(nic)) {
            throw new RuntimeException("NIC or Password mismatch!");
        } else {
            if (!userService.passwordMatchesNic(nic, password)) {
                throw new RuntimeException("Wrong password!");
            } else {
                StandardResponse standardResponse = new StandardResponse(200, "Success!", nic);
                return new ResponseEntity(standardResponse, HttpStatus.OK);
            }
        }
    }

    @PostMapping(path = "/updateIdPhoto")
    public ResponseEntity updateIdPhoto(
            @RequestParam MultipartFile file,
            @RequestParam String nic
    ) {
        deleteImage(userService.getDeletableImageName(nic));
        StandardResponse standardResponse = new StandardResponse(200, "Success!", upload(nic, file));
        return new ResponseEntity(standardResponse, HttpStatus.OK);
    }

    @PutMapping(path = "/updateUser")
    public ResponseEntity updateUser(@RequestBody UserDTO userDTO) {
        userService.updateUser(userDTO);
        StandardResponse standardResponse = new StandardResponse(200, "Success!", null);
        return new ResponseEntity(standardResponse, HttpStatus.OK);
    }


    private void deleteImage(String fileName) {
        File file = new File("/home/johash/Desktop/EasyCarRental/FrontEnd/IdImages/" + fileName);
        file.delete();
    }

    @GetMapping(path = "/getAccountDetails/{nic}")
    public ResponseEntity getAccountDetails(@PathVariable String nic){
        StandardResponse standardResponse = new StandardResponse(200, "Success!", userService.getAccountDetails(nic));
        return new ResponseEntity(standardResponse, HttpStatus.OK);
    }


}
