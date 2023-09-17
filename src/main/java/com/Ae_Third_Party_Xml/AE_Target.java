package com.Ae_Third_Party_Xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="Target")
public class AE_Target {
	private Integer Target;
    private Integer RunsRequired;
    private String OversLeft;
    private Double OriginalRunRate;
    private Double CurrentRunRate;
    private Double RequiredRunRate;

    @XmlAttribute(name = "Target")
    public Integer getTarget() {
        return Target;
    }

    public void setTarget(Integer tar) {
        this.Target = tar;
    }

    @XmlAttribute(name = "RunsRequired")
    public Integer getRunsRequired() {
        return RunsRequired;
    }

    public void setRunsRequired(Integer runs) {
        this.RunsRequired = runs;
    }

    @XmlAttribute(name = "OversLeft")
    public String getOversLeft() {
        return OversLeft;
    }

    public void setOversLeft(String overs) {
        this.OversLeft = overs;
    }

    @XmlAttribute(name = "OriginalRunRate")
    public Double getOriginalRunRate() {
        return OriginalRunRate;
    }

    public void setOriginalRunRate(Double RunRate) {
        this.OriginalRunRate = RunRate;
    }

    @XmlAttribute(name = "CurrentRunRate")
    public Double getCurrentRunRate() {
        return CurrentRunRate;
    }

    public void setCurrentRunRate(Double currentRunRate) {
        this.CurrentRunRate = currentRunRate;
    }

    @XmlAttribute(name = "RequiredRunRate")
    public Double getRequiredRunRate() {
        return RequiredRunRate;
    }

    public void setRequiredRunRate(Double requiredRunRate) {
        this.RequiredRunRate = requiredRunRate;
    }
}