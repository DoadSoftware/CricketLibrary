package com.cricket.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import javax.persistence.Column;

@Entity
@Table(name = "Teams")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team implements Comparable<Team> {

  @Id
  @Column(name = "TEAMID")
  private int teamId;
	
  @Column(name = "TeamName1")
  private String teamName1;

  @Column(name = "TeamName2")
  private String teamName2;
  
  @Column(name = "TeamName3")
  private String teamName3;
  
  @Column(name = "TeamName4")
  private String teamName4;
  
  @Column(name = "TeamGroup")
  private String teamGroup;
  
  @Column(name = "TeamBadge")
  private String teamBadge;
  
  @Column(name = "Captains")
  private String captains;
  
  @Column(name = "TeamColor")
  private String teamColor;

  @Column(name = "FullHindiTeamName")
  private String FullHindiTeamName;
  
  @Column(name = "FullTamilTeamName")
  private String FullTamilTeamName;
  
  @Column(name = "FullTeluguTeamName")
  private String FullTeluguTeamName;
  
  @Column(name = "ShortHindiTeamName")
  private String ShortHindiTeamName;
  
  @Column(name = "ShortTamilTeamName")
  private String ShortTamilTeamName;
  
  @Column(name = "ShortTeluguTeamName")
  private String ShortTeluguTeamName;
  
//public Team(String string, Integer valueOf, Integer valueOf2, Integer valueOf3, String string2, String string3,
//		String string4, Integer valueOf4, Integer valueOf5, Integer valueOf6) {
//	// TODO Auto-generated constructor stub
//}

  @Transient
  private int one;
  @Transient
  private int two;
  @Transient
  private int three;
  @Transient
  private int five;
  
  @Transient
  private List<Player> player;
  
public String getFullHindiTeamName() {
	return FullHindiTeamName;
}

public void setFullHindiTeamName(String fullHindiTeamName) {
	FullHindiTeamName = fullHindiTeamName;
}

public int getTeamId() {
	return teamId;
}

public void setTeamId(int teamId) {
	this.teamId = teamId;
}

public String getTeamName1() {
	return teamName1;
}

public void setTeamName1(String teamName1) {
	this.teamName1 = teamName1;
}

public String getTeamName2() {
	return teamName2;
}

public void setTeamName2(String teamName2) {
	this.teamName2 = teamName2;
}

public String getTeamName3() {
	return teamName3;
}

public void setTeamName3(String teamName3) {
	this.teamName3 = teamName3;
}

public String getTeamName4() {
	return teamName4;
}

public void setTeamName4(String teamName4) {
	this.teamName4 = teamName4;
}

public String getFullTamilTeamName() {
	return FullTamilTeamName;
}

public void setFullTamilTeamName(String fullTamilTeamName) {
	FullTamilTeamName = fullTamilTeamName;
}

public String getFullTeluguTeamName() {
	return FullTeluguTeamName;
}

public void setFullTeluguTeamName(String fullTeluguTeamName) {
	FullTeluguTeamName = fullTeluguTeamName;
}

public String getShortHindiTeamName() {
	return ShortHindiTeamName;
}

public void setShortHindiTeamName(String shortHindiTeamName) {
	ShortHindiTeamName = shortHindiTeamName;
}

public String getShortTamilTeamName() {
	return ShortTamilTeamName;
}

public void setShortTamilTeamName(String shortTamilTeamName) {
	ShortTamilTeamName = shortTamilTeamName;
}

public String getShortTeluguTeamName() {
	return ShortTeluguTeamName;
}

public void setShortTeluguTeamName(String shortTeluguTeamName) {
	ShortTeluguTeamName = shortTeluguTeamName;
}

public String getCaptains() {
	return captains;
}

public void setCaptains(String captains) {
	this.captains = captains;
}

public String getTeamColor() {
	return teamColor;
}

public void setTeamColor(String teamColor) {
	this.teamColor = teamColor;
}

public String getTeamGroup() {
	return teamGroup;
}

public void setTeamGroup(String teamGroup) {
	this.teamGroup = teamGroup;
}

public Team() {
	super();
	// TODO Auto-generated constructor stub
}

public int getOne() {
	return one;
}

public void setOne(int one) {
	this.one = one;
}

public int getTwo() {
	return two;
}

public void setTwo(int two) {
	this.two = two;
}

public int getThree() {
	return three;
}

public void setThree(int three) {
	this.three = three;
}

public int getFive() {
	return five;
}

public void setFive(int five) {
	this.five = five;
}

public List<Player> getPlayer() {
	return player;
}

public void setPlayer(List<Player> player) {
	this.player = player;
}


@Override
public String toString() {
	return "Team [teamId=" + teamId + ", teamName1=" + teamName1 + ", teamName2=" + teamName2 + ", teamName3="
			+ teamName3 + ", teamName4=" + teamName4 + ", teamGroup=" + teamGroup + ", teamBadge=" + teamBadge
			+ ", captains=" + captains + ", teamColor=" + teamColor + ", FullHindiTeamName=" + FullHindiTeamName
			+ ", FullTamilTeamName=" + FullTamilTeamName + ", FullTeluguTeamName=" + FullTeluguTeamName
			+ ", ShortHindiTeamName=" + ShortHindiTeamName + ", ShortTamilTeamName=" + ShortTamilTeamName
			+ ", ShortTeluguTeamName=" + ShortTeluguTeamName + ", one=" + one + ", two=" + two + ", three=" + three
			+ ", five=" + five + ", player=" + player + "]";
}

public String getTeamBadge() {
	return teamBadge;
}

public void setTeamBadge(String teamBadge) {
	this.teamBadge = teamBadge;
}

public Team(int teamId, String teamName1, String teamName4) {
	super();
	this.teamId = teamId;
	this.teamName1 = teamName1;
	this.teamName4 = teamName4;
}

public Team(int teamId, String teamName1, String teamName4, int one, int two, int three, int five) {
	super();
	this.teamId = teamId;
	this.teamName1 = teamName1;
	this.teamName4 = teamName4;
	this.one = one;
	this.two = two;
	this.three = three;
	this.five = five;
}

public Team(String teamName1, List<Player> player) {
	super();
	this.teamName1 = teamName1;
	this.player = player;
}


@Override
public int compareTo(Team tm) {
	return (int) (this.getTeamId()-tm.getTeamId());
}

}