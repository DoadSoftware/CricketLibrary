package com.Ae_Third_Party_Xml;

import javax.xml.bind.annotation.*;
@XmlRootElement(name="FallOfWicket")
public class AE_FallOfWicket {
	private Integer Wicket,Score,ID;
	private String Batsman;
	
	@XmlAttribute(name="Wicket")
	public Integer getWicket() {
		return Wicket;
	}
	public void setWicket(Integer wicket) {
		Wicket = wicket;
	}
	@XmlAttribute(name="Score")
	public Integer getScore() {
		return Score;
	}
	public void setScore(Integer score) {
		Score = score;
	}
	@XmlAttribute(name="ID")
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	@XmlAttribute(name="Batsman")
	public String getBatsman() {
		return Batsman;
	}
	public void setBatsman(String batsman) {
		Batsman = batsman;
	}
	

}
