package com.cricket.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "review", namespace = "reviews")
public class ReviewJson {

  @JsonProperty(value ="reviewNumber")
  private int reviewNumber;

  @JsonProperty(value ="reviewTeamId")
  private int reviewTeamId;

  @JsonProperty(value ="reviewStatus")
  private String reviewStatus;

  @JsonProperty(value ="reviewRetained")
  private String reviewRetained;

public ReviewJson() {
	super();
}

public ReviewJson(int reviewNumber, int reviewTeamId, String reviewStatus, String reviewRetained) {
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