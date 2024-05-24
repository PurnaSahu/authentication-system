package com.pbs.root.API_Response;

import java.util.ArrayList;

import com.pbs.root.model.Student;

public class ResultResponse {

	// here will save object of MetaData and result with Object type, we will add everything to resultResponse object
	private MetaDataResponse metaData;
	private Object result;
//	private ArrayList<Student> records;
//	
//	public ArrayList<Student> getRecords() {
//		return records;
//	}
//	public void setRecords(Student record) {
//		this.records.add(record);
//	}
	public MetaDataResponse getMetaData() {
		return metaData;
	}
	public void setMetaData(MetaDataResponse metaData) {
		this.metaData = metaData;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		System.out.println(result);
		this.result = result;
	}
	
}
