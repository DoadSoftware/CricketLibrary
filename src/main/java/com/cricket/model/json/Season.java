package com.cricket.model.json;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "Season")
public class Season {

  @Id
  @Column(name = "SeasonId")
  private int seasonId;
	
  @Column(name = "SeasonDescription")
  private String seasonDescription;

  @Column(name = "SeasonMatchTypeId")
  private int seasonMatchTypeId;

public int getSeasonId() {
	return seasonId;
}

public void setSeasonId(int seasonId) {
	this.seasonId = seasonId;
}

public String getSeasonDescription() {
	return seasonDescription;
}

public void setSeasonDescription(String seasonDescription) {
	this.seasonDescription = seasonDescription;
}

public int getSeasonMatchTypeId() {
	return seasonMatchTypeId;
}

public void setSeasonMatchTypeId(int seasonMatchTypeId) {
	this.seasonMatchTypeId = seasonMatchTypeId;
}
  
}