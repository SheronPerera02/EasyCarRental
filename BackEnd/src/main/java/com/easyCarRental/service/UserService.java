package com.easyCarRental.service;

import com.easyCarRental.dto.UserDTO;

import java.util.List;

public interface UserService {

    void registerUser(UserDTO userDTO);

    boolean nicAlreadyExists(String nic);

    List<UserDTO> getAllUsers();

    String getIdPhoto(String nic);

    boolean accountExists(String nic);

    boolean passwordMatchesNic(String nic, String password);

    void updateUser(UserDTO dto);

    String getDeletableImageName(String nic);

    UserDTO getAccountDetails(String nic);
}
