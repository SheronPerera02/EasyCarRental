package com.easyCarRental.repo;

import com.easyCarRental.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CarRepo extends JpaRepository<Car, String> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE Car SET isDamaged='Yes' WHERE registrationNo=:regNo", nativeQuery = true)
    void markDamaged(@Param("regNo") String regNo);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Car SET isAvailable='No',underMaintenance='Yes' WHERE registrationNo=:regNo", nativeQuery = true)
    void addToMaintenance(@Param("regNo") String regNo);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Car SET isAvailable='Yes',underMaintenance='No',isDamaged='No' WHERE registrationNo=:regNo", nativeQuery = true)
    void markRepaired(@Param("regNo") String regNo);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Car SET color=?2,dailyRate=?3,monthlyRate=?4,freeMileagePerDay=?5,pricePerKm=?6," +
            "frontImage=?7,backImage=?8,sideImage=?9,interiorImage=?10,freeMileagePerMonth=?11  WHERE registrationNo=?1", nativeQuery = true)
    void updateCar(String regNo, String color, double dailyRate, double monthlyRate, int freeMileagePerDay,
                   double pricePerKm, String frontImg, String backImg, String sideImg, String interiorImg, int freeMileagePerMonth);


    @Query(value = "SELECT COUNT(registrationNo) FROM Car WHERE isAvailable='Yes'", nativeQuery = true)
    int getAvailableCarCount();

    @Query(value = "SELECT COUNT(carRegNo) FROM RentalRequest,Rented_Car WHERE RentalRequest.requestId=Rented_Car.reqId AND rentStatus='Active'", nativeQuery = true)
    int getReservedCarCount();

    @Query(value = "SELECT COUNT(registrationNo) FROM Car WHERE isDamaged='Yes'", nativeQuery = true)
    int getDamagedCarCount();

    @Query(value = "SELECT COUNT(registrationNo) FROM Car WHERE underMaintenance='Yes'", nativeQuery = true)
    int getUnderMaintenanceCarCount();

    @Query(value = "SELECT * FROM Car,RentalRequest,Rented_Car WHERE Car.registrationNo=Rented_Car.carRegNo AND Rented_Car.reqId=RentalRequest.requestId AND requestId=?1", nativeQuery = true)
    List<Car> getRequestedCars(String reqId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Car SET isAvailable='No' WHERE registrationNo=?1", nativeQuery = true)
    void markUnavailable(String regNo);

    @Query(value = "SELECT type FROM Car WHERE registrationNo=?1", nativeQuery = true)
    String getCarType(String regNo);

    @Query(value = "SELECT dailyRate FROM Car WHERE registrationNo=?1", nativeQuery = true)
    double getDailyRate(String carRegNo);

    @Query(value = "SELECT monthlyRate FROM Car WHERE registrationNo=?1", nativeQuery = true)
    double getMonthlyRate(String carRegNo);

    @Query(value = "SELECT kmMeterValue FROM Car WHERE registrationNo=?1", nativeQuery = true)
    int getKmMeterValue(String carRegNo);

    @Query(value = "SELECT freeMileagePerDay FROM Car WHERE registrationNo=?1", nativeQuery = true)
    int getFreeMileagePerDay(String carRegNo);

    @Query(value = "SELECT freeMileagePerMonth FROM Car WHERE registrationNo=?1", nativeQuery = true)
    int getFreeMileagePerMonth(String carRegNo);

    @Query(value = "SELECT pricePerKm FROM Car WHERE registrationNo=?1", nativeQuery = true)
    double getPricePerKm(String carRegNo);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Car SET kmMeterValue=?2 WHERE registrationNo=?1", nativeQuery = true)
    void setNewKmMeterValue(String carRegNo, int kmMeterValue);

    @Query(value = "SELECT isAvailable FROM Car WHERE registrationNo=?1", nativeQuery = true)
    String getAvailability(String carRegNo);

}
