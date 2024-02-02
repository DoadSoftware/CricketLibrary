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
  private Team team;

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

public Team getTeam() {
	return team;
}

public void setTeam(Team team) {
	this.team = team;
}

}