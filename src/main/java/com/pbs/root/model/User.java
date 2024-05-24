package com.pbs.root.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String fullName;
	private int age;
	private String password;
	private String email;
	private String status= "Not Active";
	
	//needed tp pass the value as camelsCase, as java favours it
	@JsonProperty("otp")
	private Integer OTP;
	
	public Integer getOTP() {
		return OTP;
	}
	public void setOTP(Integer oTP) {
		OTP = oTP;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", fullName=" + fullName + ", age=" + age + ", password=" + password
				+ ", email=" + email + "]";
	}
	
//	public void isEmpty() {
//		
//	}
}
