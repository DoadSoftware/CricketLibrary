package com.Ae_Third_Party_Xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="ScheduledOvers")
public class AE_ScheduledOvers {
	private Integer originalOvers;
    private String reducedOvers;
    private String secondInningsTargetOvers;
    
    @XmlAttribute(name = "OriginalOvers")
	public Integer getOriginalOvers() {
		return originalOvers;
	}
	public void setOriginalOvers(Integer originalOvers) {
		this.originalOvers = originalOvers;
	}
	@XmlAttribute(name = "ReducedOvers")
	public String getReducedOvers() {
		return reducedOvers;
	}
	public void setReducedOvers(String reducedOvers) {
		this.reducedOvers = reducedOvers;
	}
	@XmlAttribute(name = "SecondInningsTargetOvers")
	public String getSecondInningsTargetOvers() {
		return secondInningsTargetOvers;
	}
	public void setSecondInningsTargetOvers(String secondInningsTargetOvers) {
		this.secondInningsTargetOvers = secondInningsTargetOvers;
	}
}