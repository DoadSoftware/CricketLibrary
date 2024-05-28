package com.Ae_Third_Party_Xml;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "Speed")
public class AE_Distance {
    private int values;

    @XmlValue
	public  int getValues() {
		return values;
	}

	public void setValues( int values) {
		this.values = values;
	}

    
    

}