package com.cricket.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "Venues")
public class Venue  {

  @Id
  @Column(name = "VenueId")
  private int VenueId;
	
  @Column(name = "VenueEnglishText")
  private String VenueEnglishText;

  @Column(name = "VenueHindiText")
  private String VenueHindiText;
  
  @Column(name = "VenueTamilText")
  private String VenueTamilText;
  
  @Column(name = "VenueTeluguText")
  private String VenueTeluguText;

public int getVenueId() {
	return VenueId;
}



public void setVenueId(int venueId) {
	VenueId = venueId;
}



public String getVenueEnglishText() {
	return VenueEnglishText;
}



public void setVenueEnglishText(String venueEnglishText) {
	VenueEnglishText = venueEnglishText;
}



public String getVenueHindiText() {
	return VenueHindiText;
}



public void setVenueHindiText(String venueHindiText) {
	VenueHindiText = venueHindiText;
}



public String getVenueTamilText() {
	return VenueTamilText;
}



public void setVenueTamilText(String venueTamilText) {
	VenueTamilText = venueTamilText;
}



public String getVenueTeluguText() {
	return VenueTeluguText;
}



public void setVenueTeluguText(String venueTeluguText) {
	VenueTeluguText = venueTeluguText;
}



@Override
public String toString() {
	return "Venue [VenueId=" + VenueId + ", VenueEnglishText=" + VenueEnglishText + ", VenueHindiText=" + VenueHindiText
			+ ", VenueTamilText=" + VenueTamilText + ", VenueTeluguText=" + VenueTeluguText + "]";
}


}