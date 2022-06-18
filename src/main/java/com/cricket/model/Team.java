package com.cricket.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "Teams")
public class Team implements Comparable<Team> {

  @Id
  @Column(name = "TEAMID")
  private int teamId;
	
  @Column(name = "FULLNAME")
  private String fullname;

  @Column(name = "SHORTNAME")
  private String shortname;

public int getTeamId() {
	return teamId;
}

public void setTeamId(int teamId) {
	this.teamId = teamId;
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

@Override
public String toString() {
	return "Team [teamId=" + teamId + ", fullname=" + fullname + ", shortname=" + shortname + "]";
}

@Override
public int compareTo(Team tm) {
	return (int) (this.getTeamId()-tm.getTeamId());
}

}