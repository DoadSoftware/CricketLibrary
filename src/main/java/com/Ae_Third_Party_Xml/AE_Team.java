package com.Ae_Third_Party_Xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Team")
public class AE_Team {
	@XmlAttribute(name="ShortName")
	private String ShortName;	
	public String getShortName() {
		return ShortName;
	}
	public void setShortName(String shortName) {
		ShortName = shortName;
	}
	@XmlElement(name="Player")
	private List<AE_Player_Info>Player;
	public List<AE_Player_Info> getPlayer() {
		return Player;
	}
	public void setPlayer(List<AE_Player_Info> player) {
		Player = player;
	}


	
	

}
