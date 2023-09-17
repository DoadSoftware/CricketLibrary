package com.Ae_Third_Party_Xml;

import javax.xml.bind.annotation.XmlAttribute;

public class AE_Combination {
	private Integer Batsman,Bowler,Balls,Runs;
	@XmlAttribute(name="Batsman")
	public int getBatsman() {
		return Batsman;
	}

	public void setBatsman(Integer batsman) {
		Batsman = batsman;
	}
	@XmlAttribute(name="Bowler")
	public Integer getBowler() {
		return Bowler;
	}

	public void setBowler(Integer bowler) {
		Bowler = bowler;
	}
	@XmlAttribute(name="Balls")
	public Integer getBalls() {
		return Balls;
	}

	public void setBalls(Integer balls) {
		Balls = balls;
	}
	@XmlAttribute(name="Runs")
	public Integer getRuns() {
		return Runs;
	}

	public void setRuns(Integer runs) {
		Runs = runs;
	}
	

}
