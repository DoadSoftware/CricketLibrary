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
@Table(name = "POTT")
public class POTT
{
  @Id
  @Column(name = "PoTTId")
  private int pottId;

  @Column(name = "PlayerID1")
  private int playerId1;

  @Column(name = "PlayerID2")
  private int playerId2;
  
  @Column(name = "PlayerID3")
  private int playerId3;
  
  @Column(name = "PlayerID4")
  private int playerId4;
  
  @Transient
  private Player Player1;
  
  @Transient
  private Player Player2;
  
  @Transient
  private Player Player3;
  
  @Transient
  private Player Player4;

  public POTT() {
		super();
  }

  public POTT(int pottId) {
	super();
	this.pottId = pottId;
  }

public int getPottId() {
	return pottId;
}

public void setPottId(int pottId) {
	this.pottId = pottId;
}

public int getPlayerId1() {
	return playerId1;
}

public void setPlayerId1(int playerId1) {
	this.playerId1 = playerId1;
}

public int getPlayerId2() {
	return playerId2;
}

public void setPlayerId2(int playerId2) {
	this.playerId2 = playerId2;
}

public int getPlayerId3() {
	return playerId3;
}

public void setPlayerId3(int playerId3) {
	this.playerId3 = playerId3;
}

public int getPlayerId4() {
	return playerId4;
}

public void setPlayerId4(int playerId4) {
	this.playerId4 = playerId4;
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


}