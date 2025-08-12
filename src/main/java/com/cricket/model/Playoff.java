package com.cricket.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;

@Entity
@Table(name = "Playoffs")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Playoff {

  @Id
  @Column(name = "PlayoffId")
  private int playoffId;
	
  @Column(name = "PlayoffType")
  private String playoffType;

  @Column(name = "Team1")
  private String team1;
  
  @Column(name = "team2")
  private String team2;
  
  @Column(name = "Winner")
  private String winner;
  
  @Column(name = "Date")
  private String date;

public int getPlayoffId() {
	return playoffId;
}

public void setPlayoffId(int playoffId) {
	this.playoffId = playoffId;
}

public String getPlayoffType() {
	return playoffType;
}

public void setPlayoffType(String playoffType) {
	this.playoffType = playoffType;
}

public String getTeam1() {
	return team1;
}

public void setTeam1(String team1) {
	this.team1 = team1;
}

public String getTeam2() {
	return team2;
}

public void setTeam2(String team2) {
	this.team2 = team2;
}

public String getWinner() {
	return winner;
}

public void setWinner(String winner) {
	this.winner = winner;
}


public String getDate() {
	return date;
}

public void setDate(String date) {
	this.date = date;
}

@Override
public String toString() {
	return "Playoff [playoffId=" + playoffId + ", playoffType=" + playoffType + ", team1=" + team1 + ", team2=" + team2
			+ ", winner=" + winner + ", date=" + date + "]";
}

}