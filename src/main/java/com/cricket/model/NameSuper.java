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
  
  @Column(name = "SUBHEADER")
  private String subHeader;

  @Column(name = "SUBLINE")
  private String subLine;
  
  @Column(name = "SPONSOR")
  private String sponsor;

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

public String getSubHeader() {
	return subHeader;
}

public void setSubHeader(String subHeader) {
	this.subHeader = subHeader;
}

public String getSubLine() {
	return subLine;
}

public void setSubLine(String subLine) {
	this.subLine = subLine;
}

public String getSponsor() {
	return sponsor;
}

public void setSponsor(String sponsor) {
	this.sponsor = sponsor;
}

}