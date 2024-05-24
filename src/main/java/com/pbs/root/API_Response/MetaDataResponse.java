package com.pbs.root.API_Response;

/*
 there is no such in-built classes available like MetaData/Result response for rest-api responses, i have created this class manually  
 */
public class MetaDataResponse {
	private String code;
	//success or failure message
	private String message;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getNumberOfRecords() {
		return numberOfRecords;
	}
	public void setNumberOfRecords(int numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}
	//number of records returned or affected
	private int numberOfRecords;
	
	
}
