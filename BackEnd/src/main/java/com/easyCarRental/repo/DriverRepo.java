package com.easyCarRental.repo;

import com.easyCarRental.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DriverRepo extends JpaRepository<Driver, String> {
    @Query(value = "SELECT * FROM Driver WHERE isAvailable='Yes' ORDER BY id LIMIT 1", nativeQuery = true)
    Driver getDriver();

    @Modifying
    @Transactional
    @Query(value = "UPDATE Driver SET isAvailable='No' WHERE id=?1", nativeQuery = true)
    void markUnavailable(String id);

    @Query(value = "SELECT COUNT(id) FROM Driver WHERE isAvailable='Yes'", nativeQuery = true)
    int getAvailableDriverCount();

    @Query(value = "SELECT * from Driver,Rented_Driver WHERE Rented_Driver.driverId=Driver.id AND reqId=?1", nativeQuery = true)
    List<Driver> getDriverDetails(String reqId);

    @Query(value = "SELECT COUNT(id) FROM Driver WHERE isAvailable='No'", nativeQuery = true)
    int getOccupiedDriverCount();

    @Query(value = "SELECT * FROM Driver WHERE isAvailable='Yes'", nativeQuery = true)
    List<Driver> getAllAvailableDrivers();

    @Query(value = "SELECT * FROM Driver,Rented_Driver,RentalRequest WHERE RentalRequest.requestId=Rented_Driver.reqId AND Rented_Driver.driverId=Driver.id AND requestId=?1", nativeQuery = true)
    List<Driver> getRentedDrivers(String reqId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Driver SET isAvailable='Yes' WHERE id=?1", nativeQuery = true)
    void markAvailable(String id);
}
