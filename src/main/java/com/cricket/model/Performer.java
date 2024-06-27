package com.cricket.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Performer")
public class Performer
{
  @Id
  @Column(name="PerformerId")
  private Integer performer_Id;
  
  @Column(name="PerformerType")
  private String performer_type;
  
  @Column(name="PlayerId")
  private Integer playerId;
  
  @Column(name="Text1Head")
  private String text1Head;
  
  @Column(name="Text2Head")
  private String text2Head;
  
  @Column(name="Text1Value")
  private Integer text1Value;
  
  @Column(name="Text2Value")
  private Integer text2Value;

public Integer getPerformer_Id() {
	return performer_Id;
}

public void setPerformer_Id(Integer performer_Id) {
	this.performer_Id = performer_Id;
}

public String getPerformer_type() {
	return performer_type;
}

public void setPerformer_type(String performer_type) {
	this.performer_type = performer_type;
}

public Integer getPlayerId() {
	return playerId;
}

public void setPlayerId(Integer playerId) {
	this.playerId = playerId;
}

public String getText1Head() {
	return text1Head;
}

public void setText1Head(String text1Head) {
	this.text1Head = text1Head;
}

public String getText2Head() {
	return text2Head;
}

public void setText2Head(String text2Head) {
	this.text2Head = text2Head;
}

public Integer getText1Value() {
	return text1Value;
}

public void setText1Value(Integer text1Value) {
	this.text1Value = text1Value;
}

public Integer getText2Value() {
	return text2Value;
}

public void setText2Value(Integer text2Value) {
	this.text2Value = text2Value;
}

}