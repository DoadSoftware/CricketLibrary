package com.cricket.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;

@Entity
@Table(name = "Split")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Split
{
  @Id
  @Column(name = "SplitId")
  private int splitId;

  @Column(name = "PROMPT")
  private String prompt;

  @Column(name = "TEXT1")
  private String text1;
  
  @Column(name = "TEXT2")
  private String text2;
  
  @Column(name = "TEXT3")
  private String text3;

  public Split() {
		super();
  }

  public Split(int splitId) {
	super();
	this.splitId = splitId;
  }

public int getSplitId() {
	return splitId;
}

public void setSplitId(int splitId) {
	this.splitId = splitId;
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
}