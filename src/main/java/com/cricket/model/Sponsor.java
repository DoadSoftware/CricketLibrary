package com.cricket.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;

@SuppressWarnings("unused")
@Entity
@Table(name = "Sponsor")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sponsor
{
  @Id
  @Column(name = "SponsorID")
  private int sponsorId;
  
  @Column(name = "Prompt")
  private String prompt;

  @Column(name = "ImageName")
  private String imagename;
    
  public Sponsor() {
		super();
  }

public Sponsor(int sponsorId) {
	super();
	this.sponsorId = sponsorId;
}

public int getSponsorId() {
	return sponsorId;
}

public void setSponsorId(int sponsorId) {
	this.sponsorId = sponsorId;
}

public String getPrompt() {
	return prompt;
}

public void setPrompt(String prompt) {
	this.prompt = prompt;
}

public String getImagename() {
	return imagename;
}

public void setImagename(String imagename) {
	this.imagename = imagename;
}


}