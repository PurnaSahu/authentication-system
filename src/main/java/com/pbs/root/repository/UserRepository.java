package com.pbs.root.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pbs.root.model.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	boolean existsByEmail(String mailId);

	// here the below method will return something from DB and whenever 
	//we recieve something from DB its Type will be Optional (which can hold many types of data, 
	//here those many types of data is of Student type)
	Optional<User> findByEmail(String userId);

	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.status= :status WHERE u.id= :id")
	int updateStatusById(int id, String status);

	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.OTP= :OTP WHERE u.id= :id")
	int updateOtpById(int id, int OTP);

	@Query("SELECT u FROM User u WHERE u.email= :email AND u.OTP= :otp")
	User findByMailAndOtp(String email, Integer otp);

	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.password= :password WHERE u.email= :email")
	int updatePasswordByEmail(String email, String password);

}
