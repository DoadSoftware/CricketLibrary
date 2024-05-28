package com.Ae_Third_Party_Xml;

import java.util.List;

import javax.xml.bind.annotation.*;
@XmlRootElement(name="Batsman ")
public class AE_Batsman {
	@XmlAttribute(name="Position")
	private Integer Position;
	
	@XmlAttribute(name="FielderID")
	private Integer FielderID;
	@XmlAttribute(name="BowlerID")
	private Integer BowlerID;
	@XmlAttribute(name="Runs")
	private Integer Runs;
	@XmlAttribute(name="Balls")
	private Integer Balls;
	@XmlAttribute(name="DotBalls")
	private Integer DotBalls;
	@XmlAttribute(name="Minutes")
	private Integer Minutes;
	@XmlAttribute(name="Fours")
	private Integer Fours;
	@XmlAttribute(name="Sixes")
	private Integer Sixes;
	@XmlAttribute(name="StrikeRate")
	private String StrikeRate;
	@XmlAttribute(name="Name")
	private String Name;
	@XmlAttribute(name="HowOut")
	private String HowOut;
	@XmlAttribute(name="Fielder")
	private String  Fielder;
	@XmlAttribute(name="Bowler")
	private String Bowler;
	@XmlAttribute(name="FullDismissalDescription")
	private String FullDismissalDescription;
	@XmlAttribute(name="ID")
	private Integer ID;
	@XmlAttribute(name="Facing")
	private String Facing;
	@XmlElementWrapper(name="WagonData")
	@XmlElement(name="WB")
	private List<AE_WagonData> WagonData;

	
	
	public Integer getPosition() {
		return Position;
	}
	public void setPosition(Integer position) {
		Position = position;
	}
	
	public Integer getFielderID() {
		return FielderID;
	}
	public void setFielderID(Integer fielderID) {
		FielderID = fielderID;
	}
	
	public Integer getBowlerID() {
		return BowlerID;
	}
	public void setBowlerID(Integer bowlerID) {
		BowlerID = bowlerID;
	}
	
	public Integer getRuns() {
		return Runs;
	}
	public void setRuns(Integer runs) {
		Runs = runs;
	}
	
	public Integer getBalls() {
		return Balls;
	}
	public void setBalls(Integer balls) {
		Balls = balls;
	}
	
	public Integer getDotBalls() {
		return DotBalls;
	}
	public void setDotBalls(Integer dotBalls) {
		DotBalls = dotBalls;
	}
	
	public Integer getMinutes() {
		return Minutes;
	}
	public void setMinutes(Integer minutes) {
		Minutes = minutes;
	}
	
	public Integer getFours() {
		return Fours;
	}
	public void setFours(Integer fours) {
		Fours = fours;
	}
	
	public Integer getSixes() {
		return Sixes;
	}
	public void setSixes(Integer sixes) {
		Sixes = sixes;
	}
	
	public String getStrikeRate() {
		return StrikeRate;
	}
	public void setStrikeRate(String strikeRate) {
		StrikeRate = strikeRate;
	}
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
	public String getHowOut() {
		return HowOut;
	}
	public void setHowOut(String howOut) {
		HowOut = howOut;
	}
	
	public String getFielder() {
		return Fielder;
	}
	public void setFielder(String fielder) {
		Fielder = fielder;
	}
	
	public String getBowler() {
		return Bowler;
	}
	public void setBowler(String bowler) {
		Bowler = bowler;
	}
	
	public String getFullDismissalDescription() {
		return FullDismissalDescription;
	}
	public void setFullDismissalDescription(String fullDismissalDescription) {
		FullDismissalDescription = fullDismissalDescription;
	}
	
	public Integer getId() {	
		return ID;
	}
	public Integer setId(Integer id) {
		return ID = id;
	}
	
	public String getFacing() {
		return Facing;
	}
	public void setFacing(String facing) {
		Facing = facing;
	}
	public List<AE_WagonData> getWagonData() {
		return WagonData;
	}
	public void setWagonData(List<AE_WagonData> wagonData) {
		WagonData = wagonData;
	}
	

}
