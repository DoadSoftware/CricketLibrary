package com.Ae_Third_Party_Xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Ball")
@XmlAccessorType(XmlAccessType.FIELD)
public class AE_Ball {
	private Integer Number,RunsOffBat,Byes,LegByes,Wides,NoBalls;
	private String WasItAWide,WasItANoBall,Wicket;
	
	 @XmlAttribute(name = "Number")
	public Integer getNumber() {
		return Number;
	}
	public void setNumber(Integer number) {
		Number = number;
	}
	 @XmlAttribute(name = "RunsOffBat")
	public Integer getRunsOffBat() {
		return RunsOffBat;
	}
	public void setRunsOffBat(Integer runsOffBat) {
		RunsOffBat = runsOffBat;
	}
	 @XmlAttribute(name = "Byes")
	public Integer getByes() {
		return Byes;
	}
	public void setByes(Integer byes) {
		Byes = byes;
	}
	 @XmlAttribute(name = "LegByes")
	public Integer getLegByes() {
		return LegByes;
	}
	public void setLegByes(Integer legByes) {
		LegByes = legByes;
	}
	 @XmlAttribute(name = "Wides")
	public Integer getWides() {
		return Wides;
	}
	public void setWides(Integer wides) {
		Wides = wides;
	}
	 @XmlAttribute(name = "NoBalls")
	public Integer getNoBalls() {
		return NoBalls;
	}
	public void setNoBalls(Integer noBalls) {
		NoBalls = noBalls;
	}
	 @XmlAttribute(name = "WasItAWide")
	public String getIsWasItAWide() {
		return WasItAWide;
	}
	public void setWasItAWide(String wasItAWide) {
		WasItAWide = wasItAWide;
	}
	@XmlAttribute(name = "WasItANoBall")
	public String getWasItANoBall() {
		return WasItANoBall;
	}
	public void setWasItANoBall(String wasItANoBall) {
		WasItANoBall = wasItANoBall;
	}
	 @XmlAttribute(name = "Wicket")
	public String getIsWicket() {
		return Wicket;
	}
	public void setWicket(String wicket) {
		Wicket = wicket;
	}
	

}
