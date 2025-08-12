package com.cricket.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;

@Entity
@Table(name = "VariousTexts")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VariousText {

  @Id
  @Column(name = "VariousId")
  private int variousId;
	
  @Column(name = "VariousType")
  private String variousType;

  @Column(name = "VariousText")
  private String variousText;
  
  @Column(name = "HindiVariousText")
  private String hindiVariousText;
  
  @Column(name = "TamilVariousText")
  private String tamilVariousText;
  
  @Column(name = "TeluguVariousText")
  private String teluguVariousText;
  
  @Column(name = "UseThis")
  private String useThis;

public int getVariousId() {
	return variousId;
}

public void setVariousId(int variousId) {
	this.variousId = variousId;
}

public String getVariousType() {
	return variousType;
}

public void setVariousType(String variousType) {
	this.variousType = variousType;
}

public String getVariousText() {
	return variousText;
}

public void setVariousText(String variousText) {
	this.variousText = variousText;
}

public String getHindiVariousText() {
	return hindiVariousText;
}

public void setHindiVariousText(String hindiVariousText) {
	this.hindiVariousText = hindiVariousText;
}

public String getTamilVariousText() {
	return tamilVariousText;
}

public void setTamilVariousText(String tamilVariousText) {
	this.tamilVariousText = tamilVariousText;
}

public String getTeluguVariousText() {
	return teluguVariousText;
}

public void setTeluguVariousText(String teluguVariousText) {
	this.teluguVariousText = teluguVariousText;
}

public String getUseThis() {
	return useThis;
}

public void setUseThis(String useThis) {
	this.useThis = useThis;
}
  
}