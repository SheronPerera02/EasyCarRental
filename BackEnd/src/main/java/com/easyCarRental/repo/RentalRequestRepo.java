package com.easyCarRental.repo;

import com.easyCarRental.entity.RentalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RentalRequestRepo extends JpaRepository<RentalRequest, String> {

    @Query(value = "SELECT requestId FROM RentalRequest ORDER BY requestId DESC LIMIT 1", nativeQuery = true)
    String getLastId();

    @Query(value = "SELECT rentStatus FROM RentalRequest WHERE userId=?1 ORDER BY requestId DESC LIMIT 1", nativeQuery = true)
    String getRequestStatus(String nic);

    @Query(value = "SELECT requestId FROM RentalRequest WHERE userId=?1 ORDER BY requestId DESC LIMIT 1", nativeQuery = true)
    String getRequestId(String nic);

    @Modifying
    @Transactional
    @Query(value = "UPDATE RentalRequest SET rentStatus='Cancelled' WHERE requestId=?1", nativeQuery = true)
    void setAsCancelled(String reqId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Driver,RentalRequest,Rented_Driver set isAvailable='Yes' WHERE " +
            "RentalRequest.requestId=Rented_Driver.reqId AND Rented_Driver.driverId=Driver.id AND requestId=?1",
            nativeQuery = true)
    void makeDriversAvailable(String reqId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Car,RentalRequest,Rented_Car set isAvailable='Yes' WHERE " +
            "RentalRequest.requestId=Rented_Car.reqId AND Rented_Car.carRegNo=Car.registrationNo AND requestId=?1",
            nativeQuery = true)
    void makeCarsAvailable(String reqId);

    @Query(value = "SELECT COUNT(requestId) FROM RentalRequest WHERE rentStatus='Pending' OR rentStatus='Active'", nativeQuery = true)
    int getTotalBookings();

    @Query(value = "SELECT COUNT(requestId) FROM RentalRequest WHERE rentStatus='Active'", nativeQuery = true)
    int getActiveBookings();

    @Query(value = "SELECT * FROM RentalRequest", nativeQuery = true)
    List<RentalRequest> getAllRentalRequests();

    @Modifying
    @Transactional
    @Query(value = "UPDATE Rented_Driver SET driverId=?3 WHERE driverId=?2 AND reqId=?1", nativeQuery = true)
    void changeDriver(String reqId, String driverId, String replacement);

    @Modifying
    @Transactional
    @Query(value = "UPDATE RentalRequest SET rentStatus='Active' WHERE requestId=?1", nativeQuery = true)
    void acceptRequest(String reqId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE RentalRequest SET rentStatus='Rejected',description=?2 WHERE requestId=?1", nativeQuery = true)
    void denyRequest(String reqId, String message);

    @Query(value = "SELECT description FROM RentalRequest WHERE userId=?1 ORDER BY requestId DESC LIMIT 1", nativeQuery = true)
    String getDenialMessage(String nic);

    @Query(value = "SELECT * FROM RentalRequest WHERE rentStatus='Active'", nativeQuery = true)
    List<RentalRequest> getRequestsForFinalizingPayment();

    @Query(value = "SELECT durationPlan FROM RentalRequest WHERE requestId=?1", nativeQuery = true)
    String getDurationPlan(String reqId);

    @Query(value = "SELECT pickupDate FROM RentalRequest WHERE requestId=?1", nativeQuery = true)
    String getPickupDate(String reqId);

    @Query(value = "SELECT returnDate FROM RentalRequest WHERE requestId=?1", nativeQuery = true)
    String getReturnDate(String reqId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE RentalRequest SET rentStatus='Finalized' WHERE requestId=?1", nativeQuery = true)
    void setAsFinalized(String reqId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Car,RentalRequest,Rented_Car set lastReturnDate=?2 WHERE " +
            "RentalRequest.requestId=Rented_Car.reqId AND Rented_Car.carRegNo=Car.registrationNo AND requestId=?1",
            nativeQuery = true)
    void setLastReturnDate(String reqId, String lastReturnDate);

    @Query(value = "SELECT SUM(amount) FROM Payment,RentalRequest WHERE RentalRequest.requestId=Payment.reqId AND YEAR(returnDate)=?1", nativeQuery = true)
    double getIncome(String year);
}
