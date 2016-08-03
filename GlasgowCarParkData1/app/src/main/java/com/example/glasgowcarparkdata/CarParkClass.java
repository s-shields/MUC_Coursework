package com.example.glasgowcarparkdata;
/*
 * Author: Steven Shields
 * Student ID: s1434230
 * Student User: sshiel202
 * */

//import java.io.*;

public class CarParkClass {
	
	private String carParkIdentity = "";
	private String carParkOccupancy = "";
	private String carParkStatus = ""; 
	private String occupiedSpaces = "";
	private String totalCapacity = "";
	private String carParkName = "";
	private String carParkId = "";
	private String spacesAvailable;
	private String cpMapLong = "";
	private String cpMapLat = "";
	
	
	public String getCarParkIdentity() {
		return carParkIdentity;
	}
	public void setCarParkIdentity(String carParkIdentity) {
		this.carParkIdentity = carParkIdentity;
	}
	
	public String getCarParkOccupancy() {
		return carParkOccupancy;
	}
	public void setCarParkOccupancy(String carParkOccupancy) {this.carParkOccupancy = carParkOccupancy;}
	
	public String getCarParkStatus() {
		return carParkStatus;
	}
	public void setCarParkStatus(String carParkStatus) {
		this.carParkStatus = carParkStatus;
	}
	
	public String getOccupiedSpaces() {
		return occupiedSpaces;
	}
	public void setOccupiedSpaces(String occupiedSpaces) {
		this.occupiedSpaces = occupiedSpaces;
	}
	
	public String getTotalCapacity() {
		return totalCapacity;
	}
	public void setTotalCapacity(String totalCapacity) {
		this.totalCapacity = totalCapacity;
	}	
	
	public String getCarParkName() {
		return carParkName;
	}
	public void setCarParkName(String carParkName) {
		this.carParkName = carParkName;
	}
	
	public String getCarParkId() {
		return carParkId;
	}
	public void setCarParkId(String carParkId) {
		this.carParkId = carParkId;
	}
	
	public String getSpacesAvailable() {
		return spacesAvailable;
	}
	public void setSpacesAvailable(String spacesAvailable) { this.spacesAvailable = spacesAvailable; }

	public String getCpMapLat() { return cpMapLat; }
	public void setCpMapLat(String cpMapLat) { this.cpMapLat = cpMapLat; }

	public String getCpMapLong() { return cpMapLong; }
	public void setCpMapLong(String cpMapLong) { this.cpMapLong = cpMapLong; }

	@Override
	public String toString() {
		
		return "Car Park Name: " + carParkName + "\nCar Park Id: " + carParkId 
				+ "\nCar Park Occupancy: " + carParkOccupancy +"%" + "\nSpaces Available: "
				+ spacesAvailable + "\nSpaces Used: " + occupiedSpaces
				+ "\nTotal Number of Spaces: " + totalCapacity;
	}
	
	
}
