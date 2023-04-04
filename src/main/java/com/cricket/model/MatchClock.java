package com.cricket.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="MatchClock")
@XmlAccessorType(XmlAccessType.FIELD)
public class MatchClock {

  @XmlElement(name = "inningNumber")
  private int inningNumber;
	
  @XmlElement(name = "startOrPause")
  private String startOrPause;

  @XmlElement(name = "matchTotalSeconds")
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