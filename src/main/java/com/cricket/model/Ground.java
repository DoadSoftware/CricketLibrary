package com.cricket.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "Grounds")
public class Ground {

  @Id
  @Column(name = "GROUNDID")
  private int groundId;
	
  @Column(name = "FULLNAME")
  private String fullname;

  @Column(name = "SHORTNAME")
  private String shortname;

  @Column(name = "CITY")
  private String city;

  @Column(name = "COUNTRY")
  private String country;

  @Column(name = "FIRSTBOWLINGEND")
  private String first_bowling_end;

  @Column(name = "SECONDBOWLINGEND")
  private String second_bowling_end;
  
  @Column(name = "DIMENSIONTWOOCLOCK")
  private int dimension_two_o_clock;

  @Column(name = "DIMENSIONTHREEOCLOCK")
  private int dimension_three_o_clock;

  @Column(name = "DIMENSIONFIVEOCLOCK")
  private int dimension_five_o_clock;

  @Column(name = "DIMENSIONSIXOCLOCK")
  private int dimension_six_o_clock;

  @Column(name = "DIMENSIONSEVENOCLOCK")
  private int dimension_seven_o_clock;

  @Column(name = "DIMENSIONNINEOCLOCK")
  private int dimension_nine_o_clock;

  @Column(name = "DIMENSIONTENOCLOCK")
  private int dimension_ten_o_clock;

  @Column(name = "DIMENSIONTWELVEOCLOCK")
  private int dimension_twelve_o_clock;

  @Column(name = "DIMENSIONUNIT")
  private String dimension_unit;

public int getGroundId() {
	return groundId;
}

public void setGroundId(int groundId) {
	this.groundId = groundId;
}

public String getFullname() {
	return fullname;
}

public void setFullname(String fullname) {
	this.fullname = fullname;
}

public String getShortname() {
	return shortname;
}

public void setShortname(String shortname) {
	this.shortname = shortname;
}

public String getCity() {
	return city;
}

public void setCity(String city) {
	this.city = city;
}

public String getCountry() {
	return country;
}

public void setCountry(String country) {
	this.country = country;
}

public String getFirst_bowling_end() {
	return first_bowling_end;
}

public void setFirst_bowling_end(String first_bowling_end) {
	this.first_bowling_end = first_bowling_end;
}

public String getSecond_bowling_end() {
	return second_bowling_end;
}

public void setSecond_bowling_end(String second_bowling_end) {
	this.second_bowling_end = second_bowling_end;
}

public int getDimension_two_o_clock() {
	return dimension_two_o_clock;
}

public void setDimension_two_o_clock(int dimension_two_o_clock) {
	this.dimension_two_o_clock = dimension_two_o_clock;
}

public int getDimension_three_o_clock() {
	return dimension_three_o_clock;
}

public void setDimension_three_o_clock(int dimension_three_o_clock) {
	this.dimension_three_o_clock = dimension_three_o_clock;
}

public int getDimension_five_o_clock() {
	return dimension_five_o_clock;
}

public void setDimension_five_o_clock(int dimension_five_o_clock) {
	this.dimension_five_o_clock = dimension_five_o_clock;
}

public int getDimension_six_o_clock() {
	return dimension_six_o_clock;
}

public void setDimension_six_o_clock(int dimension_six_o_clock) {
	this.dimension_six_o_clock = dimension_six_o_clock;
}

public int getDimension_seven_o_clock() {
	return dimension_seven_o_clock;
}

public void setDimension_seven_o_clock(int dimension_seven_o_clock) {
	this.dimension_seven_o_clock = dimension_seven_o_clock;
}

public int getDimension_nine_o_clock() {
	return dimension_nine_o_clock;
}

public void setDimension_nine_o_clock(int dimension_nine_o_clock) {
	this.dimension_nine_o_clock = dimension_nine_o_clock;
}

public int getDimension_ten_o_clock() {
	return dimension_ten_o_clock;
}

public void setDimension_ten_o_clock(int dimension_ten_o_clock) {
	this.dimension_ten_o_clock = dimension_ten_o_clock;
}

public int getDimension_twelve_o_clock() {
	return dimension_twelve_o_clock;
}

public void setDimension_twelve_o_clock(int dimension_twelve_o_clock) {
	this.dimension_twelve_o_clock = dimension_twelve_o_clock;
}

public String getDimension_unit() {
	return dimension_unit;
}

public void setDimension_unit(String dimension_unit) {
	this.dimension_unit = dimension_unit;
}

@Override
public String toString() {
	return "Ground [groundId=" + groundId + ", fullname=" + fullname + ", shortname=" + shortname + ", city=" + city
			+ ", country=" + country + ", first_bowling_end=" + first_bowling_end + ", second_bowling_end="
			+ second_bowling_end + ", dimension_two_o_clock=" + dimension_two_o_clock + ", dimension_three_o_clock="
			+ dimension_three_o_clock + ", dimension_five_o_clock=" + dimension_five_o_clock
			+ ", dimension_six_o_clock=" + dimension_six_o_clock + ", dimension_seven_o_clock="
			+ dimension_seven_o_clock + ", dimension_nine_o_clock=" + dimension_nine_o_clock
			+ ", dimension_ten_o_clock=" + dimension_ten_o_clock + ", dimension_twelve_o_clock="
			+ dimension_twelve_o_clock + ", dimension_unit=" + dimension_unit + "]";
}
  
}