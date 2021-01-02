package com.easyCarRental.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {
    private String nic;
    private String email;
    private String password;
    private String idPhoto;
    private String address;
    private String contact;
}
