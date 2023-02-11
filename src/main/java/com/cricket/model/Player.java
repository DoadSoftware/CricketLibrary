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

@Entity
@Table(name = "Players")
public class Player implements Comparable<Player>, Diffable<Player>
{

  @Id
  @Column(name = "PLAYERID")
  private int playerId;
	
  @Column(name = "FULLNAME")
  private String full_name;

  @Column(name = "FIRSTNAME")
  private String firstname;

  @Column(name = "SURNAME")
  private String surname;
  
  @Column(name = "TICKERNAME")
  private String ticker_name;
  
  @Column(name = "HindiFullName")
  private String hindifull_name;
  
  @Column(name = "TamilFullName")
  private String tamilfull_name;
  
  @Column(name = "TeluguFullName")
  private String telugufull_name;
  
  @Column(name = "HindiSurName")
  private String hindi_surname;
  
  @Column(name = "TamilSurName")
  private String tamil_surname;
  
  @Column(name = "TeluguSurName")
  private String telugu_surname;

  @Column(name = "ROLE")
  private String role;
  
  @Column(name = "BOWLINGSTYLE")
  private String bowlingStyle;
  
  @Column(name = "BATTINGSTYLE")
  private String battingStyle;

  @Column(name = "TEAMID")
  private Integer teamId;
  
  @Column(name = "OverseasPlayer")
  private Integer overseasPlayer;

  @Column(name = "Photo")
  private String Photo;
  
  @Column(name = "Nationality")
  private String Nationality;
  
  @Transient
  private Integer playerPosition;

  @Transient
  private String captainWicketKeeper;

  @Transient
  private String player_type;

  public Player() {
		super();
  }

  public Player(int playerId, Integer playerPosition, String player_type) {
	super();
	this.playerId = playerId;
	this.playerPosition = playerPosition;
	this.player_type = player_type;
  }
  
public String getFirstname() {
	return firstname;
}

public void setFirstname(String firstname) {
	this.firstname = firstname;
}

public String getTicker_name() {
	return ticker_name;
}

public void setTicker_name(String ticker_name) {
	this.ticker_name = ticker_name;
}

public String getPhoto() {
	return Photo;
}

public void setPhoto(String photo) {
	Photo = photo;
}

public int getPlayerId() {
	return playerId;
}

public void setPlayerId(int playerId) {
	this.playerId = playerId;
}

public String getFull_name() {
	return full_name;
}

public void setFull_name(String full_name) {
	this.full_name = full_name;
}

public String getSurname() {
	return surname;
}

public void setSurname(String surname) {
	this.surname = surname;
}

public String getRole() {
	return role;
}

public void setRole(String role) {
	this.role = role;
}

public String getBowlingStyle() {
	return bowlingStyle;
}

public void setBowlingStyle(String bowlingStyle) {
	this.bowlingStyle = bowlingStyle;
}

public String getBattingStyle() {
	return battingStyle;
}

public void setBattingStyle(String battingStyle) {
	this.battingStyle = battingStyle;
}

public Integer getTeamId() {
	return teamId;
}

public void setTeamId(Integer teamId) {
	this.teamId = teamId;
}

public Integer getPlayerPosition() {
	return playerPosition;
}

public void setPlayerPosition(Integer playerPosition) {
	this.playerPosition = playerPosition;
}

public String getCaptainWicketKeeper() {
	return captainWicketKeeper;
}

public void setCaptainWicketKeeper(String captainWicketKeeper) {
	this.captainWicketKeeper = captainWicketKeeper;
}

public String getPlayer_type() {
	return player_type;
}

public void setPlayer_type(String player_type) {
	this.player_type = player_type;
}

public String getHindifull_name() {
	return hindifull_name;
}

public void setHindifull_name(String hindifull_name) {
	this.hindifull_name = hindifull_name;
}

public String getTamilfull_name() {
	return tamilfull_name;
}

public void setTamilfull_name(String tamilfull_name) {
	this.tamilfull_name = tamilfull_name;
}

public String getTelugufull_name() {
	return telugufull_name;
}

public void setTelugufull_name(String telugufull_name) {
	this.telugufull_name = telugufull_name;
}

public String getHindi_surname() {
	return hindi_surname;
}

public void setHindi_surname(String hindi_surname) {
	this.hindi_surname = hindi_surname;
}

public String getTamil_surname() {
	return tamil_surname;
}

public void setTamil_surname(String tamil_surname) {
	this.tamil_surname = tamil_surname;
}

public String getTelugu_surname() {
	return telugu_surname;
}

public void setTelugu_surname(String telugu_surname) {
	this.telugu_surname = telugu_surname;
}

public Integer getOverseasPlayer() {
	return overseasPlayer;
}

public void setOverseasPlayer(Integer overseasPlayer) {
	this.overseasPlayer = overseasPlayer;
}

public String getNationality() {
	return Nationality;
}

public void setNationality(String nationality) {
	Nationality = nationality;
}

@Override
public DiffResult diff(Player plyr) {
	DiffBuilder db = new DiffBuilder(this, plyr, ToStringStyle.SHORT_PREFIX_STYLE);
    if ((this.captainWicketKeeper != null && !this.captainWicketKeeper.isEmpty()) || (plyr.captainWicketKeeper != null && !plyr.captainWicketKeeper.isEmpty()))
    	db.append("captainWicketKeeper", this.captainWicketKeeper, plyr.captainWicketKeeper);
    if ((this.player_type != null && !this.player_type.isEmpty()) || (plyr.player_type != null && !plyr.player_type.isEmpty()))
    	db.append("player_type", this.player_type, plyr.player_type);
    db.append("playerPosition", this.playerPosition, plyr.playerPosition);
	return db.build();
}

@Override
public int compareTo(Player pm) {
	return (int) (this.getPlayerPosition()-pm.getPlayerPosition());
}

@Override
public String toString() {
	return "Player [playerId=" + playerId + ", full_name=" + full_name + ", firstname=" + firstname + ", surname="
			+ surname + ", ticker_name=" + ticker_name + ", hindifull_name=" + hindifull_name + ", tamilfull_name="
			+ tamilfull_name + ", telugufull_name=" + telugufull_name + ", hindi_surname=" + hindi_surname
			+ ", tamil_surname=" + tamil_surname + ", telugu_surname=" + telugu_surname + ", role=" + role
			+ ", bowlingStyle=" + bowlingStyle + ", battingStyle=" + battingStyle + ", teamId=" + teamId
			+ ", overseasPlayer=" + overseasPlayer + ", Photo=" + Photo + ", Nationality=" + Nationality
			+ ", playerPosition=" + playerPosition + ", captainWicketKeeper=" + captainWicketKeeper + ", player_type="
			+ player_type + "]";
}

}