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
@Table(name = "FantasyImages")
public class FantasyImages
{
  @Id
  @Column(name = "FantasyID")
  private int fantasyId;
  
  @Column(name = "Prompt")
  private String prompt;

  @Column(name = "ImageName")
  private String imagename;
    
  public FantasyImages() {
		super();
  }

public FantasyImages(int fantasyId) {
	super();
	this.fantasyId = fantasyId;
}

public int getFantasyId() {
	return fantasyId;
}

public void setFantasyId(int fantasyId) {
	this.fantasyId = fantasyId;
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