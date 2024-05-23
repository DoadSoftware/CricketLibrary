package com.Ae_Third_Party_Xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "LastBall")
public class AE_Last_Ball {
    private List<AE_Ball_Speed> Speed;
    @XmlElement(name = "Speed")
	public List<AE_Ball_Speed> getSpeed() {
		return Speed;
	}

	public void setSpeed(List<AE_Ball_Speed> speed) {
		Speed = speed;
	}

	@Override
	public String toString() {
		return "AE_Last_Ball [Speed=" + Speed + "]";
	}
  
}