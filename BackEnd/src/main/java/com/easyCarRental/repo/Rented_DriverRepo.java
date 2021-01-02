package com.easyCarRental.repo;

import com.easyCarRental.entity.Rented_Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Rented_DriverRepo extends JpaRepository<Rented_Driver, String> {

}
