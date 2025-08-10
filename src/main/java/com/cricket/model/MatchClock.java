package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchClock {

  private int inningNumber;
	
  private String startOrPause;

  private long matchTotalSeconds;

public int getInningNumber() {
	return inningNumber;
}

public void setInningNumber(int inningNumber) {
	this.inningNumber = inningNumber;
}

public String getStartOrPause() {
	return startOrPause;
}

public void setStartOrPause(String startOrPause) {
	this.startOrPause = startOrPause;
}

public long getMatchTotalSeconds() {
	return matchTotalSeconds;
}

public void setMatchTotalSeconds(long matchTotalSeconds) {
	this.matchTotalSeconds = matchTotalSeconds;
}

@Override
public String toString() {
	return "MatchClock [inningNumber=" + inningNumber + ", startOrPause=" + startOrPause + ", matchTotalSeconds="
			+ matchTotalSeconds + "]";
}
  
}