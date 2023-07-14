package com.cricket.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "Pointers")
public class Pointers
{
  @Id
  @Column(name = "PointersId")
  private int pointersId;

  @Column(name = "PROMPT")
  private String prompt;
  
  @Column(name = "HEADER")
  private String header;

  @Column(name = "TEXT1")
  private String text1;
  
  @Column(name = "TEXT2")
  private String text2;
  
  @Column(name = "TEXT3")
  private String text3;
  
  @Column(name = "TEAM")
  private String team;
  
  @Column(name = "PLAYER")
  private String player;
  

  public Pointers() {
		super();
  }


public int getPointersId() {
	return pointersId;
}


public void setPointersId(int pointersId) {
	this.pointersId = pointersId;
}


public String getPrompt() {
	return prompt;
}


public void setPrompt(String prompt) {
	this.prompt = prompt;
}


public String getText1() {
	return text1;
}


public void setText1(String text1) {
	this.text1 = text1;
}


public String getText2() {
	return text2;
}


public void setText2(String text2) {
	this.text2 = text2;
}


public String getText3() {
	return text3;
}


public void setText3(String text3) {
	this.text3 = text3;
}


public String getTeam() {
	return team;
}


public void setTeam(String team) {
	this.team = team;
}


public String getPlayer() {
	return player;
}


public void setPlayer(String player) {
	this.player = player;
}


public String getHeader() {
	return header;
}


public void setHeader(String header) {
	this.header = header;
}

}