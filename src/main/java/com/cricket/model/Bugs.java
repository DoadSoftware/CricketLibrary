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

}