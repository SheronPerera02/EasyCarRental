package com.easyCarRental.service.impl;

import com.easyCarRental.dto.UserDTO;
import com.easyCarRental.entity.User;
import com.easyCarRental.repo.UserRepo;
import com.easyCarRental.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    ModelMapper modelMapper;

    public void registerUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        userRepo.save(user);
    }

    public boolean nicAlreadyExists(String nic) {
        return userRepo.existsById(nic);
    }

    public List<UserDTO> getAllUsers() {
        List<User> allUsers = userRepo.findAll();
        return modelMapper.map(allUsers, new TypeToken<List<UserDTO>>() {
        }.getType());
    }

    public String getIdPhoto(String nic) {
        return userRepo.getIdPhoto(nic);
    }

    public boolean accountExists(String nic) {
        return userRepo.existsById(nic);
    }

    public boolean passwordMatchesNic(String nic, String password) {
        return userRepo.getPassword(nic).contentEquals(password);
    }

    public String getDeletableImageName(String nic) {
        return userRepo.getDeletableImageName(nic);
    }

    public void updateUser(UserDTO dto) {
        userRepo.updateUser(dto.getNic(), dto.getAddress(), dto.getContact(), dto.getEmail(), dto.getIdPhoto(), dto.getPassword());
    }

    public UserDTO getAccountDetails(String nic) {
        User user = userRepo.findById(nic).get();
        return modelMapper.map(user, UserDTO.class);
    }

}
