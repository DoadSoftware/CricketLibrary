package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OverRemainingNewBall {

  private Integer oversRemaining;
  private String newBallOver;
  
public OverRemainingNewBall() {
	super();
}
public OverRemainingNewBall(Integer oversRemaining, String newBallOver) {
	super();
	this.oversRemaining = oversRemaining;
	this.newBallOver = newBallOver;
}
public Integer getOversRemaining() {
	return oversRemaining;
}
public void setOversRemaining(Integer oversRemaining) {
	this.oversRemaining = oversRemaining;
}
public String getNewBallOver() {
	return newBallOver;
}
public void setNewBallOver(String newBallOver) {
	this.newBallOver = newBallOver;
}
@Override
public String toString() {
	return "OverRemainingNewBall [oversRemaining=" + oversRemaining + ", newBallOver=" + newBallOver + "]";
}

}
