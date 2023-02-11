package com.cricket.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "Bugs")
public class Bugs
{
  @Id
  @Column(name = "BugId")
  private int bugId;

  @Column(name = "PROMPT")
  private String prompt;

  @Column(name = "TEXT1")
  private String text1;
  
  @Column(name = "TEXT2")
  private String text2;
  
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

  public Bugs() {
		super();
  }

  public Bugs(int bugId) {
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

}