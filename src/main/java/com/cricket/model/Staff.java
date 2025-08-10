package com.cricket.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;

@SuppressWarnings("unused")
@Entity
@Table(name = "Staff")
public class Staff
{
  @Id
  @Column(name = "StaffId")
  private int staffId;

  @Column(name = "NAME")
  private String staffName;

  @Column(name = "ClubId")
  private int clubId;
  
  @Column(name = "ROLE")
  private String role;
  
  @Transient
  private Team team;

  public Staff() {
		super();
  }

  public Staff(int staffId) {
	super();
	this.staffId = staffId;
  }

public int getStaffId() {
	return staffId;
}

public void setStaffId(int staffId) {
	this.staffId = staffId;
}

public String getStaffName() {
	return staffName;
}

public void setStaffName(String staffName) {
	this.staffName = staffName;
}

public int getClubId() {
	return clubId;
}

public void setClubId(int clubId) {
	this.clubId = clubId;
}

public String getRole() {
	return role;
}

public void setRole(String role) {
	this.role = role;
}

public Team getTeam() {
	return team;
}

public void setTeam(Team team) {
	this.team = team;
}

}