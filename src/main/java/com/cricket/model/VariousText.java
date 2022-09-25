package com.cricket.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "VariousTexts")
public class VariousText {

  @Id
  @Column(name = "VariousId")
  private int variousId;
	
  @Column(name = "VariousType")
  private String variousType;

  @Column(name = "VariousText")
  private String variousText;
  
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

public String getUseThis() {
	return useThis;
}

public void setUseThis(String useThis) {
	this.useThis = useThis;
}
  
}