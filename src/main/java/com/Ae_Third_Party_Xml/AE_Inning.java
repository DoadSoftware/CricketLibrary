package com.Ae_Third_Party_Xml;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Innings")
@XmlAccessorType(XmlAccessType.FIELD)
public class AE_Inning {
	
	@XmlAttribute(name="Fours")
	private Integer fours;

	public Integer getfours() {
		return fours;
	}

	public void setAe_fours(Integer fours) {
		this.fours = fours;
	}
	
	@XmlAttribute(name="Sixes")
	private Integer Sixes ;
	
	public Integer getSixes() {
		return Sixes;
	}

	public void setSixes(Integer sixes) {
		Sixes = sixes;
	}
	@XmlAttribute(name="Runs")
	private Integer Runs;
	
	public Integer getRuns() {
		return Runs;
	}

	public void setRuns(Integer runs) {
		Runs = runs;
	}
	@XmlAttribute(name="Byes")
	private Integer Byes;
	
	public Integer getByes() {
		return Byes;
	}

	public void setByes(Integer byes) {
		Byes = byes;
	}

	@XmlAttribute(name="LegByes")
	private Integer LegByes;
	public Integer getLegByes() {
		return LegByes;
	}

	public void setLegByes(Integer legByes) {
		LegByes = legByes;
	}

	
	@XmlAttribute(name="Wides")
	private Integer Wides;
	public Integer getWides() {
		return Wides;
	}

	public void setWides(Integer wides) {
		Wides = wides;
	}

	@XmlAttribute(name="NoBalls")
	private Integer NoBalls;
	
	public Integer getNoBalls() {
		return NoBalls;
	}

	public void setNoBalls(Integer noBalls) {
		NoBalls = noBalls;
	}
	@XmlAttribute(name="Penalties")
	private Integer Penalties;
	public Integer getPenalties() {
		return Penalties;
	}

	public void setPenalties(Integer penalties) {
		Penalties = penalties;
	}
	@XmlAttribute(name="Duration")
	private Integer Duration;
	public Integer getDuration() {
		return Duration;
	}

	public void setDuration(Integer duration) {
		Duration = duration;
	}
	@XmlAttribute(name="Number")
	private Integer Number;
	public Integer getNumber() {
		return Number;
	}

	public void setNumber(Integer number) {
		Number = number;
	}
	@XmlAttribute(name="NoOfWickets")
	private Integer NoOfWickets;
	public Integer getNoOfWickets() {
		return NoOfWickets;
	}

	public void setNoOfWickets(Integer noOfWickets) {
		NoOfWickets = noOfWickets;
	}
	
	@XmlElementWrapper(name="Batting")
	@XmlElement(name="Batsman")
	private List<AE_Batsman> Batsman;

	public List<AE_Batsman> getBatsman() {
		return Batsman;
	}

	public void setBatsman(List<AE_Batsman> batsman) {
		Batsman = batsman;
	}
	
	@XmlElementWrapper(name="Bowling")
	@XmlElement(name="Bowler")
	private List<AE_Bowler>Bowler;

	public List<AE_Bowler> getBowler() {
		return Bowler;
	}

	public void setBowler(List<AE_Bowler> bowler) {
		Bowler = bowler;
	}
	

	@XmlElementWrapper(name="PartnershipData")
	@XmlElement(name="Partnership")
	private List<AE_Partnership>Partnership;

	public List<AE_Partnership> getPartnership() {
		return Partnership;
	}

	public void setPartnership(List<AE_Partnership> partnership) {
		Partnership = partnership;
	}
	@XmlElementWrapper(name="HeadToHeadData")
	@XmlElement(name="Combination")
	private List<AE_Combination>Combination ;

	public List<AE_Combination> getCombination() {
		return Combination;
	}

	public void setCombination(List<AE_Combination> combination) {
		Combination = combination;
	}
	@XmlElementWrapper(name="OverData")
	@XmlElement(name="Over")
	private List<AE_Over>OverData;
	public List<AE_Over> getOverData() {
		return OverData;
	}

	public void setOverData(List<AE_Over> overData) {
		OverData = overData;
	}

	@XmlElementWrapper(name="FallOfWickets")
	@XmlElement(name="FallOfWicket ")
	private List<AE_FallOfWicket>FallOfWicket ;

	public List<AE_FallOfWicket> getFallOfWicket() {
		return FallOfWicket;
	}

	public void setFallOfWicket(List<AE_FallOfWicket> fallOfWicket) {
		FallOfWicket = fallOfWicket;
	}
	
	@XmlAttribute(name="ShortName")
	private String ShortName;
	public String getShortName() {
		return ShortName;
	}

	public void setShortName(String shortName) {
		ShortName = shortName;
	}
	@XmlAttribute(name="Status")
	private String Status;
	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}
	@XmlAttribute(name="Declared")
	private String Declared;
	public String getDeclared() {
		return Declared;
	}

	public void setDeclared(String declared) {
		Declared = declared;
	}
	@XmlAttribute(name="Overs")
	private String Overs;
	public String getOvers() {
		return Overs;
	}

	public void setOvers(String overs) {
		Overs = overs;
	}
	@XmlAttribute(name="OverRate")
	private String OverRate;
	public String getOverRate() {
		return OverRate;
	}

	public void setOverRate(String overRate) {
		OverRate = overRate;
	}
	@XmlAttribute(name="RunRate")
	private String RunRate;
	public String getRunRate() {
		return RunRate;
	}

	public void setRunRate(String runRate) {
		RunRate = runRate;
	}

	 
	
}
