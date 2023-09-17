package com.Ae_Third_Party_Xml;

import javax.xml.bind.annotation.XmlAttribute;
public class AE_Team_name {
	private String longName,ShortName;

    public String getShortName() {
		return ShortName;
	}
    @XmlAttribute(name = "ShortName")
	public void setShortName(String shortName) {
		ShortName = shortName;
	}

	@XmlAttribute(name = "LongName")
    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }
}