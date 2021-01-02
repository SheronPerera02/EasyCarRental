package com.easyCarRental.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DriverDTO {
    private String id;
    private String name;
    private String contact;
    private String isAvailable;
}
