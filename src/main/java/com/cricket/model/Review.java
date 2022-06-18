package com.cricket.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="review")
@XmlAccessorType(XmlAccessType.FIELD)
public class Review {

  @XmlElement(name = "reviewNumber")
  private int reviewNumber;

  @XmlElement(name = "reviewTeamId")
  private int reviewTeamId;

  @XmlElement(name = "reviewStatus")
  private String reviewStatus;

  @XmlElement(name = "reviewRetained")
  private String reviewRetained;

public Review() {
	super();
}

public Review(int reviewNumber, int reviewTeamId, String reviewStatus, String reviewRetained) {
	super();
	this.reviewNumber = reviewNumber;
	this.reviewTeamId = reviewTeamId;
	this.reviewStatus = reviewStatus;
	this.reviewRetained = reviewRetained;
}

public String getReviewRetained() {
	return reviewRetained;
}

public void setReviewRetained(String reviewRetained) {
	this.reviewRetained = reviewRetained;
}

public int getReviewNumber() {
	return reviewNumber;
}

public void setReviewNumber(int reviewNumber) {
	this.reviewNumber = reviewNumber;
}

public int getReviewTeamId() {
	return reviewTeamId;
}

public void setReviewTeamId(int reviewTeamId) {
	this.reviewTeamId = reviewTeamId;
}

public String getReviewStatus() {
	return reviewStatus;
}

public void setReviewStatus(String reviewStatus) {
	this.reviewStatus = reviewStatus;
}

}