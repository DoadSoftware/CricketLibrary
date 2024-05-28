package com.Ae_Third_Party_Xml;

import javax.xml.bind.annotation.*;
@XmlRootElement(name="Bowler")
public class AE_Bowler {
	private Integer Position;
	private Integer ID;
	private Integer Maidens;
	private Integer DotBalls;
	private Integer Runs;
	private Integer Wickets;
	private String Economy,Overs;
	private String BowlingNow,BowlingEnd,Name;
	 @XmlAttribute(name = "Position")
	public Integer getPosition() {
		return Position;
	}
	public void setPosition(Integer position) {
		Position = position;
	}
	 @XmlAttribute(name = "ID")
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	 @XmlAttribute(name = "Overs")
	public String getOvers() {
		return Overs;
	}
	public void setOvers(String overs) {
		Overs = overs;
	}
	 @XmlAttribute(name = "Maidens")
	public Integer getMaidens() {
		return Maidens;
	}
	public void setMaidens(Integer maidens) {
		Maidens = maidens;
	}
	 @XmlAttribute(name = "DotBalls")
	public Integer getDotBalls() {
		return DotBalls;
	}
	public void setDotBalls(Integer dotBalls) {
		DotBalls = dotBalls;
	}
	 @XmlAttribute(name = "Runs")
	public Integer getRuns() {
		return Runs;
	}
	public void setRuns(Integer runs) {
		Runs = runs;
	}
	 @XmlAttribute(name = "Wickets")
	public Integer getWickets() {
		return Wickets;
	}
	public void setWickets(Integer wickets) {
		Wickets = wickets;
	}
	 @XmlAttribute(name = "Economy")
	public String getEconomy() {
		return Economy;
	}
	public void setEconomy(String economy) {
		Economy = economy;
	}
	 @XmlAttribute(name = "BowlingNow")
	public String getBowlingNow() {
		return BowlingNow;
	}
	public void setBowlingNow(String bowlingNow) {
		BowlingNow = bowlingNow;
	}
	 @XmlAttribute(name = "BowlingEnd")
	public String getBowlingEnd() {
		return BowlingEnd;
	}
	public void setBowlingEnd(String bowlingEnd) {
		BowlingEnd = bowlingEnd;
	}
	 @XmlAttribute(name = "Name")
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
	

}
