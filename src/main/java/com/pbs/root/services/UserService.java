package com.pbs.root.services;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.pbs.root.API_Response.MetaDataResponse;
import com.pbs.root.API_Response.ResultResponse;
import com.pbs.root.model.User;
import com.pbs.root.repository.UserRepository;

import jakarta.transaction.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	 @Autowired
	 private JavaMailSender mailSender;
	
	ResultResponse response =  new ResultResponse();
	MetaDataResponse metaResponse = new MetaDataResponse();
	
	public ResultResponse registerUser(User user) {
		try {
			if (user.getFullName() == null && user.getFullName().isEmpty()
					&& user.getEmail().isEmpty()) {
				throw new Exception("User name and id can not be empty.., its manadatory field");
			} else {
				if (!userRepo.existsByEmail(user.getEmail())) {
					// String userPass= user.getPassword();
					if (isValidPassword(user.getPassword()) && isValidEmail(user.getEmail())) {
						User savedUser = userRepo.save(user);
						metaResponse.setCode("200 OK response");
						metaResponse.setMessage(
								"User Registered successfully...\n but to activate the user, please login");
						metaResponse.setNumberOfRecords(1);
						response.setMetaData(metaResponse);
						response.setResult(savedUser);
						return response;
					} else {
						throw new Exception(
								"The user id and password is not valid, Hence registration cancelled...");
					}
				} else {
					throw new Exception("Oops!! user already exist... so try login");
				}
			}
		}catch (Exception e) {
			metaResponse.setCode("400 FAIL response");
			metaResponse.setMessage(e.getMessage());
			metaResponse.setNumberOfRecords(0);
			response.setMetaData(metaResponse);
			response.setResult(null);
			return response;
		}
		//return null;
	}

	@Transactional
	public ResultResponse userLogin(String userId, String password) {
		try {
			if (userId == null && password == null) {
				throw new Exception("userID and Paasword can not be empty or NULL");
			}else {
				Optional<User> findByEmail = userRepo.findByEmail(userId);
				if(findByEmail.isEmpty())
					throw new Exception("there is no such user exist in DB!!");
				else {
					User user = findByEmail.get();
					System.out.println("user staus:"+user.getStatus());
					if( user.getPassword().equals(password)) {
						String message="User logged in successfully...";
						
						/*In Java, when comparing strings, you should use the equals() method rather than the == operator. 
						 * The == operator checks for reference equality, meaning it compares whether the two string variables refer to the same object
						 *  in memory. On the other hand, the equals() method checks for value equality, meaning it compares whether the content of 
						 *  the two strings are the same.*/
						//user.getStatus()=="Not Active" -> is wrong
						
						if(user.getStatus().equals("Not Active")) {
							System.out.println("inside status change");
							int updatedRecordCounts = userRepo.updateStatusById(user.getId(), "Active");
							if(updatedRecordCounts>0)
								message=message+" As its your first time login so, user account has been activated";
							else
								throw new Exception("Oops!! some problem occured during activation of user");
						}
						metaResponse.setCode("200 OK response from Backend");
						metaResponse.setMessage(message);
						metaResponse.setNumberOfRecords(1);
						response.setMetaData(metaResponse);
						response.setResult(findByEmail);
						return response;
					}else {
						throw new Exception("Please recheck the password!!");
					}
				}
			}
		} catch (Exception e) {
			metaResponse.setCode("400 FAIL response from Backend");
			metaResponse.setMessage(e.getMessage());
			metaResponse.setNumberOfRecords(0);
			response.setMetaData(metaResponse);
			response.setResult(null);
			return response;
		}
		//return null;
	}
	
	private boolean isValidEmail(String email) {

		// Defined the email pattern
		String emailRegex = "^(?=.{1,64}@.{4,64}$)(?=.{6,100}$)[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$";

		// Compile the pattern
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(email);

		return matcher.matches();
	}

	private boolean isValidPassword(String password) {
		
		// Check for at least one special character
		Pattern specialCharPattern = Pattern.compile("[!@#$%&*]");
		Matcher specialCharMatcher = specialCharPattern.matcher(password);

		// Check for at least one uppercase letter
		Pattern upperCasePattern = Pattern.compile("[A-Z]");
		Matcher upperCaseMatcher = upperCasePattern.matcher(password);

		// Check for at least one lowercase letter
		Pattern lowerCasePattern = Pattern.compile("[a-z]");
		Matcher lowerCaseMatcher = lowerCasePattern.matcher(password);

		return specialCharMatcher.find() && upperCaseMatcher.find() && lowerCaseMatcher.find() && password.length()>7;
	}

	/*here, will check the user passed is valid and available in our backend, if yes then will generate OTP and share it to our user*/
	@Transactional
	public ResultResponse sendOTP(String email) {
		try {
			if(userRepo.existsByEmail(email)) {
				
				/*********************6 digit OTP Generation*****************/
				Random random = new Random();
				int otpValue = 100000 + random.nextInt(900000);
				
				/*********************sending mail started*****************/
				
				String subject = "OTP Verification for password update";
				String text = "Hello Dear, \n your OTP: "+otpValue+" for mail verfication.";
				
				sendEmail(subject, text, email);
				System.out.println("Mail Acknowledgement sent succesfully...");
				
				/*********************sending mail Ended*****************/
				
				/*********************setting the OTP for the respective User in DB*****************/
				Optional<User> findByEmail = userRepo.findByEmail(email);
				User setOtpforUser = findByEmail.get();
				setOtpforUser.setOTP(otpValue);
				int updateOtpByIdCount = userRepo.updateOtpById(setOtpforUser.getId(), otpValue);
				
				metaResponse.setCode("200 OK response from backend");
				metaResponse.setMessage("OTP sent to user mail successfully...");
				metaResponse.setNumberOfRecords(updateOtpByIdCount);
				response.setMetaData(metaResponse);
				response.setResult(setOtpforUser);
				
				if(updateOtpByIdCount!=1)
					throw new Exception("OOps!! something went wrong while setting the OTP value in backend for user");
				return response;
			}else {
				throw new Exception("user not exist !!, plz check your mailID again...");
			}
		}catch (Exception e) {
			metaResponse.setCode("400 FAIL response from backend");
			metaResponse.setMessage(e.getMessage());
			metaResponse.setNumberOfRecords(0);
			response.setMetaData(metaResponse);
			response.setResult(null);
			return response;
		}
	}

	private void sendEmail(String subject, String text, String email) {
		Date date= new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String currentDate = dateFormatter.format(date);
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(email);
		mailMessage.setSubject(subject);
		mailMessage.setText(text);
		mailMessage.setSentDate(date);
		try {
			mailSender.send(mailMessage);
		}catch (Exception e) {
			//throw new Exception("Oops! something went wrong");
			e.printStackTrace();
		}
	}

	public ResultResponse verifyOTP(String email, Integer otp) {
		try {
			//String.valueOf(otp).length()==6 &&
			if( otp!= null && email != null) {
				User findByMailAndOtp = userRepo.findByMailAndOtp(email, otp);
				if(otp==findByMailAndOtp.getOTP() || findByMailAndOtp==null || findByMailAndOtp.getEmail().isEmpty() 
						|| findByMailAndOtp.getFullName().isEmpty() || findByMailAndOtp.getPassword().isEmpty()) {
					throw new Exception("OOps!! Invalid OTP, please try again...");
				}else {
					metaResponse.setCode("200 OK Response");
					metaResponse.setMessage("OTP verified succesfully..., please update your password");
					metaResponse.setNumberOfRecords(1);
					response.setMetaData(metaResponse);
					response.setResult(findByMailAndOtp);
					return response;
				}
			}
			else
				throw new Exception("Oops!! seems like you passed wrong data..");
		}catch (Exception e) {
			metaResponse.setCode("400 FAIL Response");
			metaResponse.setMessage(e.getMessage());
			metaResponse.setNumberOfRecords(0);
			response.setMetaData(metaResponse);
			response.setResult(null);
			return response;
		}
	}

	public ResultResponse updatePassword(String email, String password) {
		try {
			User user = userRepo.findByEmail(email).get();
			if(user.getPassword().equals(password))
				throw new Exception("Paasword is same, please change your password");
			else {
			int updatePasswordByEmail = userRepo.updatePasswordByEmail(email, password);
			System.out.println("Number of records affected: "+updatePasswordByEmail);
			metaResponse.setCode("200 OK");
			metaResponse.setMessage("Password updated successfully...");
			metaResponse.setNumberOfRecords(updatePasswordByEmail);
			response.setMetaData(metaResponse);
			response.setResult(user);
			return response;
			}
		}catch (Exception e) {
			metaResponse.setCode("200 OK");
			metaResponse.setMessage(e.getMessage());
			metaResponse.setNumberOfRecords(0);
			response.setMetaData(metaResponse);
			response.setResult(null);
			return response;
		}
	}



}
