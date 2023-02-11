package com.cricket.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "Profile")
public class Profile {

  @Id
  @Column(name = "PROFILEID")
  private int profile_id;
	
  @Column(name = "PROFILEHEADERTEXT")
  private String profile_header_text;

  @Column(name = "STATSTYPEID")
  private int stats_type_id;
  
  @Column(name = "ShortText")
  private int ShortText;

public int getProfile_id() {
	return profile_id;
}

public void setProfile_id(int profile_id) {
	this.profile_id = profile_id;
}

public String getProfile_header_text() {
	return profile_header_text;
}

public void setProfile_header_text(String profile_header_text) {
	this.profile_header_text = profile_header_text;
}

public int getStats_type_id() {
	return stats_type_id;
}

public void setStats_type_id(int stats_type_id) {
	this.stats_type_id = stats_type_id;
}

public int getShortText() {
	return ShortText;
}

public void setShortText(int shortText) {
	ShortText = shortText;
}

}