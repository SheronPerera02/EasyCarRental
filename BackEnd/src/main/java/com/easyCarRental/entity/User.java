package com.easyCarRental.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    @Id
    private String nic;
    private String email;
    private String password;
    private String idPhoto;
    private String address;
    private String contact;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<RentalRequest> rentalRequestList = new ArrayList<RentalRequest>();

}
