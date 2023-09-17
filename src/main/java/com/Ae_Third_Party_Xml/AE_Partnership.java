package com.Ae_Third_Party_Xml;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="Partnership")
public class AE_Partnership {
	private String Bat1Name,Bat2Name;
	private int Runs,Fours,Sixes,Bat2ID,Bat1ID,Balls,Number,Bat1Runs,Bat2Runs,Bat1Balls,Bat2Balls;
	 @XmlAttribute(name = "Bat1Name")
	public String getBat1Name() {
		return Bat1Name;
	}
	public void setBat1Name(String bat1Name) {
		Bat1Name = bat1Name;
	}
	 @XmlAttribute(name = "Bat1Nmae")
	public String getBat2Name() {
		return Bat2Name;
	}
	public void setBat2Name(String bat2Name) {
		Bat2Name = bat2Name;
	}
	 @XmlAttribute(name = "Runs")
	public int getRuns() {
		return Runs;
	}
	public void setRuns(int runs) {
		Runs = runs;
	}
	 @XmlAttribute(name = "Fours")
	public int getFours() {
		return Fours;
	}
	public void setFours(int fours) {
		Fours = fours;
	}
	 @XmlAttribute(name = "Sixes")
	public int getSixes() {
		return Sixes;
	}
	public void setSixes(int sixes) {
		Sixes = sixes;
	}
	 @XmlAttribute(name = "Bat2ID")
	public int getBat2ID() {
		return Bat2ID;
	}
	public void setBat2ID(int bat2id) {
		Bat2ID = bat2id;
	}
	 @XmlAttribute(name = "Bat1ID")
	public int getBat1ID() {
		return Bat1ID;
	}
	public void setBat1ID(int bat1id) {
		Bat1ID = bat1id;
	}
	 @XmlAttribute(name = "Balls")
	public int getBalls() {
		return Balls;
	}
	public void setBalls(int balls) {
		Balls = balls;
	}
	 @XmlAttribute(name = "Number")
	public int getNumber() {
		return Number;
	}
	public void setNumber(int number) {
		Number = number;
	}
	 @XmlAttribute(name = "Bat1Runs")
	public int getBat1Runs() {
		return Bat1Runs;
	}
	public void setBat1Runs(int bat1Runs) {
		Bat1Runs = bat1Runs;
	}
	 @XmlAttribute(name = "Bat2Runs")
	public int getBat2Runs() {
		return Bat2Runs;
	}
	public void setBat2Runs(int bat2Runs) {
		Bat2Runs = bat2Runs;
	}
	 @XmlAttribute(name = "Bat1Balls")
	public int getBat1Balls() {
		return Bat1Balls;
	}
	public void setBat1Balls(int bat1Balls) {
		Bat1Balls = bat1Balls;
	}
	 @XmlAttribute(name = "Bat2Balls")
	public int getBat2Balls() {
		return Bat2Balls;
	}
	public void setBat2Balls(int bat2Balls) {
		Bat2Balls = bat2Balls;
	}
	

}
