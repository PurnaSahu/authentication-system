package com.pbs.root.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pbs.root.API_Response.ResultResponse;
import com.pbs.root.model.Student;
import com.pbs.root.services.StudentService;

//starting point of our request for studentController will be set with with RequestMapping
@RestController
@RequestMapping("/student-api")
public class StudentController {

	//create an object of student service to use here
	@Autowired
	private StudentService stuService;
	
	@GetMapping("/fetchStudents")
	private ResultResponse fetchAll() {
		return stuService.fetchAll();
	}
	@GetMapping("/fetchByID/{id}")
	private ResultResponse fetchById(@PathVariable int id) {
		return stuService.fetchById(id);
	}
	@DeleteMapping("/deleteBy/{id}/fromStudents")
	private  ResultResponse deleteByID(@PathVariable  ("id") int id){
		return stuService.deleteById(id);
	}
	@PutMapping("/updateStudent/{id}")
	private ResultResponse updateStudent(@RequestBody Student studData, @PathVariable int id) {
		return stuService.updateStudent(studData, id);
	}
	@PostMapping("/save")
	private ResultResponse saveStudent(@RequestBody Student stud) {
		return stuService.saveStudent(stud);
	}
	@PostMapping("/saveStudents")
	private ResultResponse saveStudents(@RequestBody ArrayList<Student> stud) {
		return stuService.saveStudents(stud);
	}
	
}
