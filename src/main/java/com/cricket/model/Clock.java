package com.cricket.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Clock")
@XmlAccessorType(XmlAccessType.FIELD)
public class Clock{

	@XmlElement(name = "matchHalves")
	  private String matchHalves;
	  
	  @XmlElement(name = "matchTimeStatus")
	  private String matchTimeStatus;

	  @XmlElement(name = "matchTotalMilliSeconds")
	  private long matchTotalMilliSeconds;

	public String getMatchHalves() {
		return matchHalves;
	}

	public void setMatchHalves(String matchHalves) {
		this.matchHalves = matchHalves;
	}

	public String getMatchTimeStatus() {
		return matchTimeStatus;
	}

	public void setMatchTimeStatus(String matchTimeStatus) {
		this.matchTimeStatus = matchTimeStatus;
	}

	public long getMatchTotalMilliSeconds() {
		return matchTotalMilliSeconds;
	}

	public void setMatchTotalMilliSeconds(long matchTotalMilliSeconds) {
		this.matchTotalMilliSeconds = matchTotalMilliSeconds;
	}
	  


}