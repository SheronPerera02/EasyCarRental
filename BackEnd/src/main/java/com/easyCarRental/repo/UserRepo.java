package com.easyCarRental.repo;

import com.easyCarRental.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

    @Query(value = "SELECT idPhoto FROM User WHERE nic=:nic", nativeQuery = true)
    String getIdPhoto(@Param("nic") String nic);

    @Query(value = "SELECT password FROM User WHERE nic=?1", nativeQuery = true)
    String getPassword(String nic);

    @Query(value = "SELECT COUNT(nic) FROM User", nativeQuery = true)
    int getUserCount();

    @Modifying
    @Transactional
    @Query(value = "UPDATE User SET address=?2,contact=?3,email=?4,idPhoto=?5,password=?6 WHERE nic=?1", nativeQuery = true)
    void updateUser(String nic, String address, String contact, String email, String idPhoto, String password);

    @Query(value = "SELECT idPhoto FROM User WHERE nic=?1", nativeQuery = true)
    String getDeletableImageName(String nic);

}
