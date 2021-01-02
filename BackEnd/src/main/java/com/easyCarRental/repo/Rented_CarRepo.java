package com.easyCarRental.repo;

import com.easyCarRental.entity.Rented_Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Rented_CarRepo extends JpaRepository<Rented_Car, String> {

}
