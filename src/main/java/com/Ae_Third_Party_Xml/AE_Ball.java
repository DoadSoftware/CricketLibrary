package com.Ae_Third_Party_Xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Ball")
@XmlAccessorType(XmlAccessType.FIELD)
public class AE_Ball {
	@XmlAttribute(name = "Number")
	private Integer Number;
	@XmlAttribute(name = "RunsOffBat")
	private Integer RunsOffBat;
	@XmlAttribute(name = "Byes")
	private Integer Byes;
	 @XmlAttribute(name = "LegByes")
	private Integer LegByes;
	 @XmlAttribute(name = "Wides")
	private Integer Wides;
	 @XmlAttribute(name = "NoBalls")
	private Integer NoBalls;
	 @XmlAttribute(name = "WasItAWide")
	private String WasItAWide;
	 @XmlAttribute(name = "WasItANoBall")
	private String WasItANoBall;
	 @XmlAttribute(name = "Wicket")
	private String Wicket;
	
	 
	public Integer getNumber() {
		return Number;
	}
	public void setNumber(Integer number) {
		this.Number = number;
	}
	 
	public Integer getRunsOffBat() {
		return RunsOffBat;
	}
	public void setRunsOffBat(Integer runsOffBat) {
		this.RunsOffBat = runsOffBat;
	}
	 
	public Integer getByes() {
		return Byes;
	}
	public void setByes(Integer byes) {
		this.Byes = byes;
	}
	
	public Integer getLegByes() {
		return LegByes;
	}
	public void setLegByes(Integer legByes) {
		this.LegByes = legByes;
	}
	
	public Integer getWides() {
		return Wides;
	}
	public void setWides(Integer wides) {
		this.Wides = wides;
	}
	 
	public Integer getNoBalls() {
		return NoBalls;
	}
	public void setNoBalls(Integer noBalls) {
		this.NoBalls = noBalls;
	}
	 
	public String getIsWasItAWide() {
		return WasItAWide;
	}
	public void setWasItAWide(String wasItAWide) {
		this.WasItAWide = wasItAWide;
	}
	
	public String getWasItANoBall() {
		return WasItANoBall;
	}
	public void setWasItANoBall(String wasItANoBall) {
		this.WasItANoBall = wasItANoBall;
	}
	
	public String getIsWicket() {
		return Wicket;
	}
	public void setWicket(String wicket) {
		this.Wicket = wicket;
	}
	

}
