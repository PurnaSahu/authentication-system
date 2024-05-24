package com.pbs.root.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.pbs.root.API_Response.MetaDataResponse;
import com.pbs.root.API_Response.ResultResponse;
import com.pbs.root.model.Student;
import com.pbs.root.repository.StudentRepository;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studRepo;
	
	ResultResponse resultResponse = new ResultResponse();
	MetaDataResponse metadataRes = new MetaDataResponse();
	
	public ResultResponse fetchAll() {
		try {
			// this studRepo only designed for Student entity so it will fetch data from Student DB only
			int numberOfRecords = studRepo.findAll().size();
			metadataRes.setCode("200K");
			if (numberOfRecords==0)
				metadataRes.setMessage("Ooops!! there is no data found for student...");
			else {
			metadataRes.setMessage("All messages imported from DB successfully...");
			}
			metadataRes.setNumberOfRecords(numberOfRecords);
			resultResponse.setMetaData(metadataRes);
			resultResponse.setResult(studRepo.findAll());
			return resultResponse;
		}catch (Exception e) {
			e.printStackTrace();
			metadataRes.setCode("400K");
			metadataRes.setMessage("Ooops!! unable to fetch data...");
			metadataRes.setNumberOfRecords(0);
			resultResponse.setMetaData(metadataRes);
			resultResponse.setResult(null);
			return resultResponse;
		}
	}

	public ResultResponse fetchById(int id) {
		try {
			Optional<Student> findById = studRepo.findById(id);
			if (findById == null || findById.isEmpty()) {
				//metadataRes.setCode("400K");
				//metadataRes.setMessage("The id info you want to fetch, not found in DB !!");
				//metadataRes.setNumberOfRecords(0);
				//resultResponse.setMetaData(metadataRes);
				//resultResponse.setResult(null);
				/*the above format also we can set the message or we can send a custom error message to catch block and there set
				 * result response properties*/
				throw new Exception("The id ="+id+" info you want to fetch, not found in DB !!");
			}else {
				Student idInfo = findById.get();
				System.out.println("the id info: "+idInfo);
				metadataRes.setCode("200K");
				metadataRes.setMessage("Information fetched successfully...");
				metadataRes.setNumberOfRecords(1);
				resultResponse.setMetaData(metadataRes);
				resultResponse.setResult(findById);
			}
			return resultResponse;
		}catch (Exception e) {
			e.getMessage();
			metadataRes.setCode("400K");
			metadataRes.setMessage(e.getMessage());
			metadataRes.setNumberOfRecords(0);
			resultResponse.setMetaData(metadataRes);
			resultResponse.setResult(null);
			return resultResponse;
		}
		//return null;
	}
	private boolean checkUniqueStudent(String mailId) { 
//	String errorMessage=null;
//	if()
//		errorMessage= "Oops !! student already exists with same username / mailId ...";
	return studRepo.existsByEmail(mailId);
	}
	
	public ResultResponse saveStudent( Student stud) {
		try {
			if(stud== null || stud.isEmpty()) {
				throw new Exception("You must need to provide User Info to save in DB !!");
			}
			if(studRepo.existsById(stud.getId())) {
				throw new Exception("Oops !! student with same ID already available...");
			}
			//String isUnique= checkUniqueStudent(stud.getEmail());
			if (checkUniqueStudent(stud.getEmail()))
				throw new Exception("Oops !! student already exists with same username / mailId ..");
			
			// studRepo.save() returns the same data, once its saved in DB successfully which is of type student
			Student savedInfo = studRepo.save(stud);
			System.out.println("Student data which saved in DB: "+savedInfo);
			
			if(savedInfo== null || savedInfo.isEmpty())
				throw new Exception("Ooops seems like data not saved in DB properly !!");
			
			metadataRes.setCode("200 with OK response");
			metadataRes.setNumberOfRecords(1);
			metadataRes.setMessage("Student Info saved in DB successfully...");
			resultResponse.setMetaData(metadataRes);
			resultResponse.setResult(savedInfo);
			return resultResponse;
		}catch (Exception e) {
			metadataRes.setCode("400 with FAIL response");
			metadataRes.setNumberOfRecords(0);
			metadataRes.setMessage(e.getMessage());
			resultResponse.setMetaData(metadataRes);
			resultResponse.setResult(null);
			return resultResponse;
		}
		//return null;
	}
	
	public ResultResponse deleteById(int id) {
		try {
			if(studRepo.existsById(id)) {
				Optional<Student> findById = studRepo.findById(id);
				//this method returns void, it will simply delete the data if its exists
				studRepo.deleteById(id);
				metadataRes.setCode("200 OK response from Backend...");
				metadataRes.setNumberOfRecords(1);
				metadataRes.setMessage("student info deleted successfully with ID: "+id);
				resultResponse.setMetaData(metadataRes);
				resultResponse.setResult(findById);
				return resultResponse;
			}else {
				throw new Exception("OOps!! There is no student with ID: "+id+" found in DB");
				//break;
			}
		}catch (EmptyResultDataAccessException p) {
			//if the id that we wants to delete not found in DB, most probably we will get this error, as we already handled the 
			//situation above with if,else logic sothis block wont execute at all
		}
		catch (Exception e) {
			metadataRes.setCode("400 FAIL response from Backend...");
			metadataRes.setNumberOfRecords(0);
			metadataRes.setMessage(e.getMessage());
			resultResponse.setMetaData(metadataRes);
			resultResponse.setResult(null);
			return resultResponse;
		}
		//bcoz of else section, we also need to keep return statement here
		return resultResponse;
	}
	
	public ResultResponse updateStudent(Student studData, int id) {
		try {
			if(studRepo.existsById(id)) {
				Optional<Student> stuIdToUpdate = studRepo.findById(id);
				/*here will create a student object reference and we can pass the old data(stuIdToUpdate) to student object, 
				 * the only reason we doing this is to fetch individual properties of the student object and 
				 * then will correct each property of student*/
				Student correctData= stuIdToUpdate.get();
				System.out.println("old data of student ID: "+id+" is: "+correctData);
				correctData.setName(studData.getName());
				correctData.setAge(studData.getAge());
				correctData.setDept(studData.getDept());
				correctData.setPhone(studData.getPhone());
				correctData.setState(studData.getState());
				correctData.setEmail(studData.getEmail());
//				if(studRepo.existsByEmail(studData.getEmail())) {
//					throw new Exception("OOps!! There is already a student exists with userName: "+studData.getEmail());
//				}
//				else {
					metadataRes.setCode("200 OK response from Backend");
					metadataRes.setMessage("successfully updated the info for student with ID: "+id+" & mail: "+correctData.getEmail());
					metadataRes.setNumberOfRecords(1);
					resultResponse.setMetaData(metadataRes);
					resultResponse.setResult(studRepo.save(correctData));
					return resultResponse;
				//}
			}else {
				throw new Exception("OOps!! There is no student with ID: "+id+" found in DB");
			}
		}catch (Exception e) {
			metadataRes.setCode("400 FAIL response from Backend");
			metadataRes.setMessage(e.getMessage());
			metadataRes.setNumberOfRecords(0);
			resultResponse.setMetaData(metadataRes);
			resultResponse.setResult(null);
			return resultResponse;
		}
		//return null;
	}
	
	ArrayList<Student> skippedStudents;
	public ResultResponse saveStudents(ArrayList<Student> stud) {
		//ArrayList<Student> skippedStudents;
		try {
			int recordCount=0;
			if(stud.isEmpty())
				throw new Exception("Ooops!! No student data provided..");
			for(Student stu : stud) {
				if (checkUniqueStudent(stu.getEmail())) {
					skippedStudents.add(stu);
					continue;
					//throw new Exception("Oops !! student already exists with same username / mailId ..");
				}
				Student savedRecord = studRepo.save(stu);
				//resultResponse.setRecords(savedRecord);
				++recordCount;
			}
			metadataRes.setCode("200 OK");
//			if (skippedStudents==null)
//				metadataRes.setNumberOfRecords(stud.size());
//			else
			metadataRes.setNumberOfRecords(recordCount);
			metadataRes.setMessage("Successfully inserted data");
			resultResponse.setMetaData(metadataRes);
			//resultResponse.setResult(stud);
			return resultResponse;
		}catch (Exception e) {
			metadataRes.setCode("400 with FAIL response");
			metadataRes.setNumberOfRecords(0);
			metadataRes.setMessage(e.getMessage());
			resultResponse.setMetaData(metadataRes);
			resultResponse.setResult(null);
			return resultResponse;
		}
		//return null;
	}

}
