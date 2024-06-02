package com.Ae_Third_Party_Xml;

import java.util.List;

import javax.xml.bind.annotation.*;

public class AE_Over {
	@XmlAttribute(name="Runs")
	private Integer Runs;
	@XmlAttribute(name="RunRate")
	private String RunRate;
	@XmlAttribute(name="No")
	private Integer No;
	@XmlElement(name="Wicket")
	private List <AE_Wicket> Wicket;
	
	
	
	public Integer getNo() {
		return No;
	}
	public void setNo(Integer no) {
		this.No = no;
	}
	
	public Integer getRuns() {
		return Runs;
	}
	public void setRuns(Integer run) {
		this.Runs = run;
	}
	
	public String getRunRate() {
		return RunRate;
	}
	public void setRunRate(String runRate) {
		this.RunRate = runRate;
	}
	
	public List <AE_Wicket> getWicket() {
		return Wicket;
	}
	public void setWicket(List <AE_Wicket> wicket) {
		this.Wicket = wicket;
	}
	

}
