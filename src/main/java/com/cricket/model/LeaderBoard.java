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
  
  @Column(name = "Header")
  private String header;
  
  @Column(name = "SubHeader")
  private String subHeader;
  
  @Column(name = "StatType")
  private String StatType;
  
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

public int getLeaderboardId() {
	return leaderboardId;
}

public void setLeaderboardId(int leaderboardId) {
	this.leaderboardId = leaderboardId;
}

public String getHeader() {
	return header;
}

public void setHeader(String header) {
	this.header = header;
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

@Override
public String toString() {
	return "LeaderBoard [leaderboardId=" + leaderboardId + ", header=" + header + ", subHeader=" + subHeader
			+ ", player1Id=" + player1Id + ", player2Id=" + player2Id + ", player3Id=" + player3Id + ", player4Id="
			+ player4Id + ", player5Id=" + player5Id + ", PlayerStats1=" + PlayerStats1 + ", playerStats2="
			+ playerStats2 + ", playerStats3=" + playerStats3 + ", playerStats4=" + playerStats4 + ", playerStats5="
			+ playerStats5 + ", Player1=" + Player1 + ", Player2=" + Player2 + ", Player3=" + Player3 + ", Player4="
			+ Player4 + ", Player5=" + Player5 + "]";
}

}