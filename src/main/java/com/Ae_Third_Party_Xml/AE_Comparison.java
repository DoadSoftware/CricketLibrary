package com.Ae_Third_Party_Xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="Comparision")
public class AE_Comparison {
	 	private String CompOvers;
	    private String Team1Name;
	    private String Team1Score;
	    private String Team2Name;
	    private String Team2Score;
	    
	    @XmlAttribute(name = "CompOvers")
		public String getCompOvers() {
			return CompOvers;
		}
		public void setCompOvers(String comp) {
			this.CompOvers = comp;
		}
		@XmlAttribute(name = "Team1Name")
		public String getTeam1Name() {
			return Team1Name;
		}
		public void setTeam1Name(String team1Name) {
			this.Team1Name = team1Name;
		}
		@XmlAttribute(name = "Team1Score")
		public String getTeam1Score() {
			return Team1Score;
		}
		public void setTeam1Score(String team1Score) {
			this.Team1Score = team1Score;
		}
		@XmlAttribute(name = "Team2name")
		public String getTeam2Name() {
			return Team2Name;
		}
		public void setTeam2Name(String team2Name) {
			this.Team2Name = team2Name;
		}
		@XmlAttribute(name = "Team2Score")
		public String getTeam2Score() {
			return Team2Score;
		}
		public void setTeam2Score(String team2Score) {
			this.Team2Score = team2Score;
		}

	}
