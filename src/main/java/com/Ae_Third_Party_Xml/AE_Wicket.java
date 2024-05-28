package com.Ae_Third_Party_Xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Wicket")
public class AE_Wicket {
	@XmlAttribute(name="ID")
	private String ident;

	public String getId() {
		return ident;
	}

	public void setId(String id) {
		this.ident = id;
	}
	

}
