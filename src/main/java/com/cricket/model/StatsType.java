package com.cricket.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "StatsType")
public class StatsType {

  @Id
  @Column(name = "STATSID")
  private int stats_id;
	
  @Column(name = "STATSFULLNAME")
  private String stats_full_name;

  @Column(name = "STATSSHORTNAME")
  private String stats_short_name;

public int getStats_id() {
	return stats_id;
}

public void setStats_id(int stats_id) {
	this.stats_id = stats_id;
}

public String getStats_full_name() {
	return stats_full_name;
}

public void setStats_full_name(String stats_full_name) {
	this.stats_full_name = stats_full_name;
}

public String getStats_short_name() {
	return stats_short_name;
}

public void setStats_short_name(String stats_short_name) {
	this.stats_short_name = stats_short_name;
}

}