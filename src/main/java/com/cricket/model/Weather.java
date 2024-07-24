package com.cricket.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "Weather")
public class Weather {

  @Id
  @Column(name = "WeatherId")
  private int weatherId;
	
  @Column(name = "CurrentTemp")
  private String currentTemp;

  @Column(name = "Humidity")
  private String humidity;
  
  @Column(name = "LowestTemp")
  private String lowestTemp;
  
  @Column(name = "HighestTemp")
  private String highestTemp;
  
  @Column(name = "WindDirection")
  private String windDirection;
  
  @Column(name = "WindSpeed")
  private String windSpeed;

public int getWeatherId() {
	return weatherId;
}

public void setWeatherId(int weatherId) {
	this.weatherId = weatherId;
}

public String getCurrentTemp() {
	return currentTemp;
}

public void setCurrentTemp(String currentTemp) {
	this.currentTemp = currentTemp;
}

public String getHumidity() {
	return humidity;
}

public void setHumidity(String humidity) {
	this.humidity = humidity;
}

public String getLowestTemp() {
	return lowestTemp;
}

public void setLowestTemp(String lowestTemp) {
	this.lowestTemp = lowestTemp;
}

public String getHighestTemp() {
	return highestTemp;
}

public void setHighestTemp(String highestTemp) {
	this.highestTemp = highestTemp;
}

public String getWindDirection() {
	return windDirection;
}

public void setWindDirection(String windDirection) {
	this.windDirection = windDirection;
}

public String getWindSpeed() {
	return windSpeed;
}

public void setWindSpeed(String windSpeed) {
	this.windSpeed = windSpeed;
}


  
}