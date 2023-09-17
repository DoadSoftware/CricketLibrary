package com.Ae_Third_Party_Xml;

import javax.xml.bind.annotation.*;
@XmlRootElement(name="Batsman")
public class AE_Batsman {
	private Integer Position,FielderID,BowlerID,Runs,Balls,DotBalls,Minutes,Fours,Sixes;
	private String StrikeRate;
	private String Name,HowOut,Fielder,Bowler,FullDismissalDescription;
	
	@XmlAttribute(name="Position")
	public Integer getPosition() {
		return Position;
	}
	public void setPosition(Integer position) {
		Position = position;
	}
	@XmlAttribute(name="FielderID")
	public Integer getFielderID() {
		return FielderID;
	}
	public void setFielderID(Integer fielderID) {
		FielderID = fielderID;
	}
	@XmlAttribute(name="BowlerID")
	public Integer getBowlerID() {
		return BowlerID;
	}
	public void setBowlerID(Integer bowlerID) {
		BowlerID = bowlerID;
	}
	@XmlAttribute(name="Runs")
	public Integer getRuns() {
		return Runs;
	}
	public void setRuns(Integer runs) {
		Runs = runs;
	}
	@XmlAttribute(name="Balls")
	public Integer getBalls() {
		return Balls;
	}
	public void setBalls(Integer balls) {
		Balls = balls;
	}
	@XmlAttribute(name="DotBalls")
	public Integer getDotBalls() {
		return DotBalls;
	}
	public void setDotBalls(Integer dotBalls) {
		DotBalls = dotBalls;
	}
	@XmlAttribute(name="Minutes")
	public Integer getMinutes() {
		return Minutes;
	}
	public void setMinutes(Integer minutes) {
		Minutes = minutes;
	}
	@XmlAttribute(name="Fours")
	public Integer getFours() {
		return Fours;
	}
	public void setFours(Integer fours) {
		Fours = fours;
	}
	@XmlAttribute(name="Sixes")
	public Integer getSixes() {
		return Sixes;
	}
	public void setSixes(Integer sixes) {
		Sixes = sixes;
	}
	@XmlAttribute(name="StrikeRate")
	public String getStrikeRate() {
		return StrikeRate;
	}
	public void setStrikeRate(String strikeRate) {
		StrikeRate = strikeRate;
	}
	@XmlAttribute(name="Name")
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	@XmlAttribute(name="HowOut")
	public String getHowOut() {
		return HowOut;
	}
	public void setHowOut(String howOut) {
		HowOut = howOut;
	}
	@XmlAttribute(name="Fielder")
	public String getFielder() {
		return Fielder;
	}
	public void setFielder(String fielder) {
		Fielder = fielder;
	}
	@XmlAttribute(name="Bowler")
	public String getBowler() {
		return Bowler;
	}
	public void setBowler(String bowler) {
		Bowler = bowler;
	}
	@XmlAttribute(name="FullDismissalDescription")
	public String getFullDismissalDescription() {
		return FullDismissalDescription;
	}
	public void setFullDismissalDescription(String fullDismissalDescription) {
		FullDismissalDescription = fullDismissalDescription;
	}
	@XmlAttribute(name="ID")
	private Integer ID;
	public Integer getId() {	
		return ID;
	}
	public Integer setId(Integer id) {
		return ID = id;
	}
	

}
