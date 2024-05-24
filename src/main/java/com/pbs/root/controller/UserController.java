package com.pbs.root.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pbs.root.API_Response.ResultResponse;
import com.pbs.root.model.User;
import com.pbs.root.services.UserService;

@RestController
@RequestMapping("/student-auth")
public class UserController {

	/*===============Authentication of User=================*/
	/*   
	   Register User
	   validating User data, B4 accepting them in our Application
	   Login User
	   Forgot Password Feature
	   OTP verification
	   Mail Acknowledgement
	 */
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	private ResultResponse registerUser(@RequestBody User user) {
		return userService.registerUser(user);
		/*User on registration, By default not activated. only on 1st time login the user will be activated*/
	}
	@PostMapping("/login")
	private ResultResponse userLogin(@RequestBody User user) {
		String userId= user.getEmail();
		String password= user.getPassword();
		return userService.userLogin(userId, password);
	}
	
	/*As the user forgot the password, we will pass the mailID in RequestBody and verify the mailID is valid. if yes, we will generate
	   OTP and send it via mail*/
	@PostMapping("/forgetPassword/sendOTP")
	private ResultResponse sendOTP(@RequestBody User user) {
		return userService.sendOTP(user.getEmail());
	}
	
	/*once the user recieved the OTP, the OTP will be shared along with mailID to confirm the authentication of user*/
	@PostMapping("/forgetPassword/verifyOTP")
	private ResultResponse verifyOTP(@RequestBody User user) {
		System.out.println("mail:"+user.getEmail()+" OTP: "+user.getOTP());
		return userService.verifyOTP(user.getEmail(), user.getOTP());
	}
	
	/*now the user will provide the updated password*/
	@PutMapping("/forgetPassword/updatePassword")
	private ResultResponse updatePassword(@RequestBody User user) {
		return userService.updatePassword(user.getEmail(), user.getPassword());
	}
}
