package com.Ae_Third_Party_Xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="CurrentPosition")
public class AE_CurrentPosition {
	private String  CurrentInningsRuns;
	private String CurrentInningsWickets; 
	private String CurrentOversBowled ;
	private String CurrentOddBallsBowled; 
	private String CurrentBatsmanOnStrike; 
	private String CurrentBatsmanOnStrikeRuns ;
	private String CurrentBatsmanOnStrikeBalls ; 
	private String CurrentBatsmanNonStriker ;
	private String CurrentBatsmanNonStrikerRuns ; 
	private String CurrentBatsmanNonStrikerBalls ; 
	private String CurrentBowler  ;
	private String CurrentBowlerWickets ;
	private String CurrentBowlerRuns;
	
	 @XmlAttribute(name = "CurrentInningsRuns")
	public String getCurrentInningsRuns() {
		return CurrentInningsRuns;
	}
	public void setCurrentInningsRuns(String currentInningsRuns) {
		CurrentInningsRuns = currentInningsRuns;
	}
	 @XmlAttribute(name = "CurrentInningsWickets")
	public String getCurrentInningsWickets() {
		return CurrentInningsWickets;
	}
	public void setCurrentInningsWickets(String currentInningsWickets) {
		CurrentInningsWickets = currentInningsWickets;
	}
	 @XmlAttribute(name = "CurrentOversBowled")
	public String getCurrentOversBowled() {
		return CurrentOversBowled;
	}
	public void setCurrentOversBowled(String currentOversBowled) {
		CurrentOversBowled = currentOversBowled;
	}
	 @XmlAttribute(name = "CurrentOddBallsBowled")
	public String getCurrentOddBallsBowled() {
		return CurrentOddBallsBowled;
	}
	public void setCurrentOddBallsBowled(String currentOddBallsBowled) {
		CurrentOddBallsBowled = currentOddBallsBowled;
	}
	 @XmlAttribute(name = "CurrentBatsmanOnStrike")
	public String getCurrentBatsmanOnStrike() {
		return CurrentBatsmanOnStrike;
	}
	public void setCurrentBatsmanOnStrike(String currentBatsmanOnStrike) {
		CurrentBatsmanOnStrike = currentBatsmanOnStrike;
	}
	 @XmlAttribute(name = "CurrentBatsmanOnStrikeRuns")
	public String getCurrentBatsmanOnStrikeRuns() {
		return CurrentBatsmanOnStrikeRuns;
	}
	public void setCurrentBatsmanOnStrikeRuns(String currentBatsmanOnStrikeRuns) {
		CurrentBatsmanOnStrikeRuns = currentBatsmanOnStrikeRuns;
	}
	 @XmlAttribute(name = "CurrentBatsmanOnStrikeBalls")
	public String getCurrentBatsmanOnStrikeBalls() {
		return CurrentBatsmanOnStrikeBalls;
	}
	public void setCurrentBatsmanOnStrikeBalls(String currentBatsmanOnStrikeBalls) {
		CurrentBatsmanOnStrikeBalls = currentBatsmanOnStrikeBalls;
	}
	 @XmlAttribute(name = "CurrentBatsmanNonStriker")
	public String getCurrentBatsmanNonStriker() {
		return CurrentBatsmanNonStriker;
	}
	public void setCurrentBatsmanNonStriker(String currentBatsmanNonStriker) {
		CurrentBatsmanNonStriker = currentBatsmanNonStriker;
	}
	 @XmlAttribute(name = "CurrentBatsmanNonStrikerRuns")
	public String getCurrentBatsmanNonStrikerRuns() {
		return CurrentBatsmanNonStrikerRuns;
	}
	public void setCurrentBatsmanNonStrikerRuns(String currentBatsmanNonStrikerRuns) {
		CurrentBatsmanNonStrikerRuns = currentBatsmanNonStrikerRuns;
	}
	 @XmlAttribute(name = "CurrentBatsmanNonStrikerBalls")
	public String getCurrentBatsmanNonStrikerBalls() {
		return CurrentBatsmanNonStrikerBalls;
	}
	public void setCurrentBatsmanNonStrikerBalls(String currentBatsmanNonStrikerBalls) {
		CurrentBatsmanNonStrikerBalls = currentBatsmanNonStrikerBalls;
	}
	 @XmlAttribute(name = "CurrentBowler")
	public String getCurrentBowler() {
		return CurrentBowler;
	}
	public void setCurrentBowler(String currentBowler) {
		CurrentBowler = currentBowler;
	}
	 @XmlAttribute(name = "CurrentBowlerWickets")
	public String getCurrentBowlerWickets() {
		return CurrentBowlerWickets;
	}
	public void setCurrentBowlerWickets(String currentBowlerWickets) {
		CurrentBowlerWickets = currentBowlerWickets;
	}
	 @XmlAttribute(name = "CurrentBowlerRuns")
	public String getCurrentBowlerRuns() {
		return CurrentBowlerRuns;
	}
	public void setCurrentBowlerRuns(String currentBowlerRuns) {
		CurrentBowlerRuns = currentBowlerRuns;
	}
	

}
