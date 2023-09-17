package com.Ae_Third_Party_Xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="Status")
public class AE_Status {
	private String  PlayInProgress, CurrentBattingTeamShortName, OverBallByBall,Heading, Situation;
	private Integer CurrentInnings;
	@XmlAttribute(name="PlayInProgress")
	public String getPlayInProgress() {
		return PlayInProgress;
	}
	public void setPlayInProgress(String playInProgress) {
		PlayInProgress = playInProgress;
	}
	@XmlAttribute(name="CurrentBattingTeamShortName")
	public String getCurrentBattingTeamShortName() {
		return CurrentBattingTeamShortName;
	}
	public void setCurrentBattingTeamShortName(String currentBattingTeamShortName) {
		CurrentBattingTeamShortName = currentBattingTeamShortName;
	}
	@XmlAttribute(name="OverBallByBall")
	public String getOverBallByBall() {
		return OverBallByBall;
	}
	public void setOverBallByBall(String overBallByBall) {
		OverBallByBall = overBallByBall;
	}
	@XmlAttribute(name="Heading")
	public String getHeading() {
		return Heading;
	}
	public void setHeading(String heading) {
		Heading = heading;
	}
	@XmlAttribute(name="Situation")
	public String getSituation() {
		return Situation;
	}
	public void setSituation(String situation) {
		Situation = situation;
	}
	@XmlAttribute(name="CurrentInnings")
	public Integer getCurrentInnings() {
		return CurrentInnings;
	}
	public void setCurrentInnings(Integer currentInnings) {
		CurrentInnings = currentInnings;
	}

	}
