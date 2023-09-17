package com.Ae_Third_Party_Xml;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="Match")
public class AE_Match {

	  private String matchType;
	    private Integer ballPerOver;
	    private Integer numberOfPlayersPerTeam;
	    @XmlAttribute(name = "MatchType")
		public String getMatchType() {
			return matchType;
		}
		public void setMatchType(String matchType) {
			this.matchType = matchType;
		}
		@XmlAttribute(name = "BallPerOver")
		public Integer getBallPerOver() {
			return ballPerOver;
		}
		public void setBallPerOver(Integer ballPerOver) {
			this.ballPerOver = ballPerOver;
		}
		@XmlAttribute(name = "NumberOfPlayersPerTeam")
		public Integer getNumberOfPlayersPerTeam() {
			return numberOfPlayersPerTeam;
		}
		public void setNumberOfPlayersPerTeam(Integer numberOfPlayersPerTeam) {
			this.numberOfPlayersPerTeam = numberOfPlayersPerTeam;
		}
	}
