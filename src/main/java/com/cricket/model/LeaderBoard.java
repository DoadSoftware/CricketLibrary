package com.cricket.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Column;

@Entity
@Table(name = "LeaderBoard")
public class LeaderBoard {

  @Id
  @Column(name = "LeaderboardId")
  private int leaderboardId;
  
  @Column(name = "Header1")
  private String header1;
  
  @Column(name = "Header2")
  private String header2;
  
  @Column(name = "SubHeader")
  private String subHeader;
  
  @Column(name = "Logo")
  private String logo;
  
  @Column(name = "Highlight")
  private int highlight;
  
  @Column(name = "StatType")
  private String statType;
  
  @Column(name = "Player1")
  private int player1Id;
  
  @Column(name = "Player2")
  private int player2Id;
  
  @Column(name = "Player3")
  private int player3Id;
  
  @Column(name = "Player4")
  private int player4Id;
  
  @Column(name = "Player5")
  private int player5Id;
  
  @Column(name = "PlayerStats1")
  private String PlayerStats1;
  
  @Column(name = "PlayerStats2")
  private String playerStats2;
  
  @Column(name = "PlayerStats3")
  private String playerStats3;
  
  @Column(name = "PlayerStats4")
  private String playerStats4;
  
  @Column(name = "PlayerStats5")
  private String playerStats5;
  
  @Transient
  private Player Player1;
  
  @Transient
  private Player Player2;
  
  @Transient
  private Player Player3;
  
  @Transient
  private Player Player4;
  
  @Transient
  private Player Player5;
  
  @Transient
  private Team team1;
  
  @Transient
  private Team team2;
  
  @Transient
  private Team team3;
  
  @Transient
  private Team team4;
  
  @Transient
  private Team team5;

public int getLeaderboardId() {
	return leaderboardId;
}

public void setLeaderboardId(int leaderboardId) {
	this.leaderboardId = leaderboardId;
}

public String getHeader1() {
	return header1;
}

public void setHeader1(String header) {
	this.header1 = header;
}

public String getHeader2() {
	return header2;
}

public void setHeader2(String header2) {
	this.header2 = header2;
}

public String getSubHeader() {
	return subHeader;
}

public void setSubHeader(String subHeader) {
	this.subHeader = subHeader;
}

public int getPlayer1Id() {
	return player1Id;
}

public void setPlayer1Id(int player1Id) {
	this.player1Id = player1Id;
}

public int getPlayer2Id() {
	return player2Id;
}

public void setPlayer2Id(int player2Id) {
	this.player2Id = player2Id;
}

public int getPlayer3Id() {
	return player3Id;
}

public void setPlayer3Id(int player3Id) {
	this.player3Id = player3Id;
}

public int getPlayer4Id() {
	return player4Id;
}

public void setPlayer4Id(int player4Id) {
	this.player4Id = player4Id;
}

public int getPlayer5Id() {
	return player5Id;
}

public void setPlayer5Id(int player5Id) {
	this.player5Id = player5Id;
}

public String getPlayerStats1() {
	return PlayerStats1;
}

public void setPlayerStats1(String playerStats1) {
	PlayerStats1 = playerStats1;
}

public String getPlayerStats2() {
	return playerStats2;
}

public void setPlayerStats2(String playerStats2) {
	this.playerStats2 = playerStats2;
}

public String getPlayerStats3() {
	return playerStats3;
}

public void setPlayerStats3(String playerStats3) {
	this.playerStats3 = playerStats3;
}

public String getPlayerStats4() {
	return playerStats4;
}

public void setPlayerStats4(String playerStats4) {
	this.playerStats4 = playerStats4;
}

public String getPlayerStats5() {
	return playerStats5;
}

public void setPlayerStats5(String playerStats5) {
	this.playerStats5 = playerStats5;
}

public Player getPlayer1() {
	return Player1;
}

public void setPlayer1(Player player1) {
	Player1 = player1;
}

public Player getPlayer2() {
	return Player2;
}

public void setPlayer2(Player player2) {
	Player2 = player2;
}

public Player getPlayer3() {
	return Player3;
}

public void setPlayer3(Player player3) {
	Player3 = player3;
}

public Player getPlayer4() {
	return Player4;
}

public void setPlayer4(Player player4) {
	Player4 = player4;
}

public Player getPlayer5() {
	return Player5;
}

public void setPlayer5(Player player5) {
	Player5 = player5;
}
public String getStatType() {
	return statType;
}

public void setStatType(String statType) {
	this.statType = statType;
}

public Team getTeam1() {
	return team1;
}

public void setTeam1(Team team1) {
	this.team1 = team1;
}

public Team getTeam2() {
	return team2;
}

public void setTeam2(Team team2) {
	this.team2 = team2;
}

public Team getTeam3() {
	return team3;
}

public void setTeam3(Team team3) {
	this.team3 = team3;
}

public Team getTeam4() {
	return team4;
}

public void setTeam4(Team team4) {
	this.team4 = team4;
}

public Team getTeam5() {
	return team5;
}

public void setTeam5(Team team5) {
	this.team5 = team5;
}

public String getLogo() {
	return logo;
}

public void setLogo(String logo) {
	this.logo = logo;
}

public int getHighlight() {
	return highlight;
}

public void setHighlight(int highlight) {
	this.highlight = highlight;
}

@Override
public String toString() {
	return "LeaderBoard [leaderboardId=" + leaderboardId + ", header1=" + header1 + ", header2=" + header2
			+ ", subHeader=" + subHeader + ", logo=" + logo + ", highlight=" + highlight + ", statType=" + statType
			+ ", player1Id=" + player1Id + ", player2Id=" + player2Id + ", player3Id=" + player3Id + ", player4Id="
			+ player4Id + ", player5Id=" + player5Id + ", PlayerStats1=" + PlayerStats1 + ", playerStats2="
			+ playerStats2 + ", playerStats3=" + playerStats3 + ", playerStats4=" + playerStats4 + ", playerStats5="
			+ playerStats5 + ", Player1=" + Player1 + ", Player2=" + Player2 + ", Player3=" + Player3 + ", Player4="
			+ Player4 + ", Player5=" + Player5 + ", team1=" + team1 + ", team2=" + team2 + ", team3=" + team3
			+ ", team4=" + team4 + ", team5=" + team5 + "]";
}

}