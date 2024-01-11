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
@Table(name = "NameSupers")
public class NameSuper
{
  @Id
  @Column(name = "NAMESUPERID")
  private int namesuperId;

  @Column(name = "FIRSTNAME")
  private String firstname;

  @Column(name = "SURNAME")
  private String surname;
  
  @Column(name = "Prompt")
  private String prompt;

  @Column(name = "SUBLINE")
  private String subLine;
  
  @Column(name = "FLAG")
  private String flag;
  
  @Column(name = "SPONSOR")
  private String sponsor;

  @Column(name = "HindiFirstName")
  private String HindiFirstName;
  
  @Column(name = "HindiSurName")
  private String HindiSurName;
  
  @Column(name = "TamilFirstName")
  private String TamilFirstName;
  
  @Column(name = "TamilSurName")
  private String TamilSurName;
  
  @Column(name = "TeluguFirstName")
  private String TeluguFirstName;
  
  @Column(name = "TeluguSurName")
  private String TeluguSurName;
  
  public NameSuper() {
		super();
  }

  public NameSuper(int namesuperId) {
	super();
	this.namesuperId = namesuperId;
  }
  
public int getNamesuperId() {
	return namesuperId;
}

public void setNamesuperId(int namesuperId) {
	this.namesuperId = namesuperId;
}

public String getFirstname() {
	return firstname;
}

public void setFirstname(String firstname) {
	this.firstname = firstname;
}

public String getSurname() {
	return surname;
}

public void setSurname(String surname) {
	this.surname = surname;
}

public String getPrompt() {
	return prompt;
}

public void setPrompt(String prompt) {
	this.prompt = prompt;
}

public String getSubLine() {
	return subLine;
}

public void setSubLine(String subLine) {
	this.subLine = subLine;
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

public String getHindiFirstName() {
	return HindiFirstName;
}

public void setHindiFirstName(String hindiFirstName) {
	HindiFirstName = hindiFirstName;
}

public String getHindiSurName() {
	return HindiSurName;
}

public void setHindiSurName(String hindiSurName) {
	HindiSurName = hindiSurName;
}

public String getTamilFirstName() {
	return TamilFirstName;
}

public void setTamilFirstName(String tamilFirstName) {
	TamilFirstName = tamilFirstName;
}

public String getTamilSurName() {
	return TamilSurName;
}

public void setTamilSurName(String tamilSurName) {
	TamilSurName = tamilSurName;
}

public String getTeluguFirstName() {
	return TeluguFirstName;
}

public void setTeluguFirstName(String teluguFirstName) {
	TeluguFirstName = teluguFirstName;
}

public String getTeluguSurName() {
	return TeluguSurName;
}

public void setTeluguSurName(String teluguSurName) {
	TeluguSurName = teluguSurName;
}

}