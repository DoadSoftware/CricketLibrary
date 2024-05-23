package com.Ae_Third_Party_Xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "LastSixDistance")
public class AE_Six_Distance {
    private List<AE_Distance> Distance;
    @XmlElement(name = "InMetres")
	public List<AE_Distance> getDistance() {
		return Distance;
	}

	public void setDistance(List<AE_Distance> distance) {
		Distance = distance;
	}

	@Override
	public String toString() {
		return "AE_Six_Distance [Distance=" + Distance + "]";
	}
    
	
  
}
