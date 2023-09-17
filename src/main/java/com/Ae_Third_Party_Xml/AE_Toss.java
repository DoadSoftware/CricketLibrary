package com.Ae_Third_Party_Xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="Toss")
public class AE_Toss {
	private String WinnerID;
    private String Decision;

    @XmlAttribute(name = "WinnerID")
    public String getWinnerID() {
        return WinnerID;
    }

    public void setWinnerID(String winnerID) {
        this.WinnerID = winnerID;
    }

    @XmlAttribute(name = "Decision")
    public String getDecision() {
        return Decision;
    }

    public void setDecision(String decision) {
        this.Decision = decision;
    }
}