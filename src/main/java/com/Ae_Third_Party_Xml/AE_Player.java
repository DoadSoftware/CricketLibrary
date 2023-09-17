package com.Ae_Third_Party_Xml;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Player ")
@XmlAccessorType(XmlAccessType.FIELD)
public class AE_Player {

	private Integer Matches,Runs, Wickets,Identity;
	private String BatAverage,BatStrikeRate;
	private String BestBowl,BestBat,BowlAverage;

	 @XmlAttribute(name = "ID")
	public Integer getID() {
		return Identity;
	}

	public void setID(Integer iD) {
		Identity = iD;
	}
	 @XmlAttribute(name = "Matches")
	public Integer getMatches() {
		return Matches;
	}

	public void setMatches(Integer matches) {
		Matches = matches;
	}
	 @XmlAttribute(name = "Runs")
	public Integer getRuns() {
		return Runs;
	}

	public void setRuns(Integer runs) {
		Runs = runs;
	}
	 @XmlAttribute(name = "BestBat")
	public String getBestBat() {
		return BestBat;
	}

	public void setBestBat(String bestBat) {
		BestBat = bestBat;
	}
	 @XmlAttribute(name = "BatAverage")
	public String getBatAverage() {
		return BatAverage;
	}

	public void setBatAverage(String batAverage) {
		BatAverage = batAverage;
	}
	 @XmlAttribute(name = "BatStrikeRate")
	public String getBatStrikeRate() {
		return BatStrikeRate;
	}

	public void setBatStrikeRate(String batStrikeRate) {
		BatStrikeRate = batStrikeRate;
	}
	 @XmlAttribute(name = "Wickets")
	public Integer getWickets() {
		return Wickets;
	}

	public void setWickets(Integer wickets) {
		Wickets = wickets;
	}
	 @XmlAttribute(name = "BowlAverage")
	public String getBowlAverage() {
		return BowlAverage;
	}

	public void setBowlAverage(String bowlAverage) {
		BowlAverage = bowlAverage;
	}
	 @XmlAttribute(name = "BestBowl")
	public String getBestBowl() {
		return BestBowl;
	}

	public void setBestBowl(String bestBowl) {
		BestBowl = bestBowl;
	}
	

}
