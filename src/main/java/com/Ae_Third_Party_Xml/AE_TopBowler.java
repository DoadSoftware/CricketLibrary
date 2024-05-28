package com.Ae_Third_Party_Xml;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Player")
@XmlAccessorType(XmlAccessType.FIELD)
public class AE_TopBowler {

	@XmlAttribute(name="ID")
	private Integer ID;

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}
	
}
