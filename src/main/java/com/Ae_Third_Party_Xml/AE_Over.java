package com.Ae_Third_Party_Xml;

import javax.xml.bind.annotation.*;

public class AE_Over {
	private Integer No,Runs;
	private String RunRate;
	private Wicket Wicket;
	@XmlAttribute(name="No")
	public Integer getNo() {
		return No;
	}
	public void setNo(Integer no) {
		No = no;
	}
	@XmlAttribute(name="Runs")
	public Integer getRuns() {
		return Runs;
	}
	public void setRuns(Integer run) {
		Runs = run;
	}
	@XmlAttribute(name="RunRate")
	public String getRunRate() {
		return RunRate;
	}
	public void setRunRate(String runRate) {
		RunRate = runRate;
	}
	@XmlElement(name="Wicket")
	public Wicket getWicket() {
		return Wicket;
	}
	public void setWicket(Wicket wicket) {
		Wicket = wicket;
	}
	

}
class Wicket{
	private String ID;
	@XmlAttribute(name="ID")
	public String getID() {
		return ID;
	}
}