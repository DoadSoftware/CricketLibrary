package com.cricket.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;

@Entity
@Table(name = "PerformanceBug")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PerformanceBug
{
  @Id
  @Column(name = "BugId")
  private int bugId;

  @Column(name = "PROMPT")
  private String prompt;

  @Column(name = "PLAYER_NAME")
  private String playerName;
  
  @Column(name = "SCORE")
  private String score;
  
  @Column(name = "SUBHEADER")
  private String subheader;
  
  @Column(name = "TEXT4")
  private String text4;
  
  @Column(name = "Flag")
  private String flag;
  
  @Column(name = "Sponsor")
  private String sponsor;
  
  @Column(name = "HindiText1")
  private String hindiText1;
  
  @Column(name = "HindiText2")
  private String hindiText2;
  
  @Column(name = "TamilText1")
  private String tamilText1;
  
  @Column(name = "TamilText2")
  private String tamilText2;
  
  @Column(name = "TeluguText1")
  private String teluguText1;
  
  @Column(name = "TeluguText2")
  private String teluguText2;

  public PerformanceBug() {
		super();
  }

  public PerformanceBug(int bugId) {
	super();
	this.bugId = bugId;
  }

public int getBugId() {
	return bugId;
}

public void setBugId(int bugId) {
	this.bugId = bugId;
}

public String getPrompt() {
	return prompt;
}

public void setPrompt(String prompt) {
	this.prompt = prompt;
}

public String getHindiText1() {
	return hindiText1;
}

public void setHindiText1(String hindiText1) {
	this.hindiText1 = hindiText1;
}

public String getHindiText2() {
	return hindiText2;
}

public void setHindiText2(String hindiText2) {
	this.hindiText2 = hindiText2;
}

public String getTamilText1() {
	return tamilText1;
}

public void setTamilText1(String tamilText1) {
	this.tamilText1 = tamilText1;
}

public String getTamilText2() {
	return tamilText2;
}

public void setTamilText2(String tamilText2) {
	this.tamilText2 = tamilText2;
}

public String getTeluguText1() {
	return teluguText1;
}

public void setTeluguText1(String teluguText1) {
	this.teluguText1 = teluguText1;
}

public String getTeluguText2() {
	return teluguText2;
}

public void setTeluguText2(String teluguText2) {
	this.teluguText2 = teluguText2;
}

public String getPlayerName() {
	return playerName;
}

public void setPlayerName(String playerName) {
	this.playerName = playerName;
}

public String getScore() {
	return score;
}

public void setScore(String score) {
	this.score = score;
}

public String getSubheader() {
	return subheader;
}

public void setSubheader(String subheader) {
	this.subheader = subheader;
}

public String getText4() {
	return text4;
}

public void setText4(String text4) {
	this.text4 = text4;
}

public String getFlag() {
	return flag;
}

public void setFlag(String flag) {
	this.flag = flag;
}

public String getSponsor() {
	return sponsor;
}

public void setSponsor(String sponsor) {
	this.sponsor = sponsor;
}

}