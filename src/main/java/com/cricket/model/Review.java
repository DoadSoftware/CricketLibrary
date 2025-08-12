package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Review {

  private int reviewNumber;
  private int reviewTeamId;
  private String reviewStatus;
  private String reviewRetained;
  private long lastTimeStamp;

public Review() {
	super();
}

public Review(String reviewStatus, long lastTimeStamp) {
	super();
	this.reviewStatus = reviewStatus;
	this.lastTimeStamp = lastTimeStamp;
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

public long getLastTimeStamp() {
	return lastTimeStamp;
}

public void setLastTimeStamp(long lastTimeStamp) {
	this.lastTimeStamp = lastTimeStamp;
}

}