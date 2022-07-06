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
@Table(name = "InfobarStats")
public class InfobarStats
{
  @Id
  @Column(name = "ORDER")
  private int order;

  @Column(name = "PROMPT")
  private String prompt;

  @Column(name = "TEXT1")
  private String text1;
  
  @Column(name = "TEXT2")
  private String text2;

  public InfobarStats() {
		super();
  }

  public InfobarStats(int order) {
	super();
	this.order = order;
  }

public int getOrder() {
	return order;
}

public void setOrder(int order) {
	this.order = order;
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