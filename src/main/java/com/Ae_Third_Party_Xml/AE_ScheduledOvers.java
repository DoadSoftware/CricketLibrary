package com.Ae_Third_Party_Xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="ScheduledOvers")
public class AE_ScheduledOvers {
	private Integer originalOvers;
    private String reducedOvers;
    private Integer secondInningsTargetOvers;
    
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
	public Integer getSecondInningsTargetOvers() {
		return secondInningsTargetOvers;
	}
	public void setSecondInningsTargetOvers(Integer secondInningsTargetOvers) {
		this.secondInningsTargetOvers = secondInningsTargetOvers;
	}
}