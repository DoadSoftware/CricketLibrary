package com.Ae_Third_Party_Xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="MatchDetails")
@XmlAccessorType(XmlAccessType.FIELD)
public class AE_MatchDetails {
	private AE_Team_name HomeTeam;
    private AE_Team_name AwayTeam;
    private AE_Toss Toss;
    private AE_Target Target;
    private AE_Match Match;
    private AE_Ground Ground;
	private AE_ScheduledOvers ScheduledOvers;
    private AE_Comparison Comparison;
    private AE_Status Status;
    
    @XmlElement(name="Status")
    public AE_Status getStatus() {
        return Status;
    }
    public void setStatus(AE_Status status) {
		this.Status = status;
	}

	@XmlElement(name="HomeTeam")
    public AE_Team_name getHomeTeam() {
        return HomeTeam;
    }
   
    public void setHomeTeam(AE_Team_name homeTeam) {
        this.HomeTeam = homeTeam;
    }
    @XmlElement(name="AwayTeam")
    public AE_Team_name getAwayTeam() {
        return AwayTeam;
    }

    public void setAwayTeam(AE_Team_name awayTeam) {
        this.AwayTeam = awayTeam;
    }
    @XmlElement(name="Toss")
    public AE_Toss getToss() {
        return Toss;
    }

    public void setToss(AE_Toss tos) {
        this.Toss = tos;
    }
    @XmlElement(name="Target")
    public AE_Target getTarget() {
        return Target;
    }

    public void setTarget(AE_Target taret) {
        this.Target = taret;
    }
    @XmlElement(name="Match")
	public AE_Match getMatch() {
		return Match;
	}

	public void setMatch(AE_Match matchs) {
		this.Match = matchs;
	}
	 @XmlElement(name="Ground")
	public AE_Ground getGround() {
		return Ground;
	}

	public void setGround(AE_Ground grounds) {
		this.Ground = grounds;
	}
	 @XmlElement(name="ScheduledOvers")
	public AE_ScheduledOvers getScheduledOvers() {
		return ScheduledOvers;
	}

	public void setScheduledOvers(AE_ScheduledOvers scheduledOver) {
		this.ScheduledOvers = scheduledOver;
	}
	@XmlElement(name="Comparison")
	public AE_Comparison getComparison() {
		return Comparison;
	}

	public void setComparison(AE_Comparison comparisons) {
		this.Comparison = comparisons;
	}

	}
    


