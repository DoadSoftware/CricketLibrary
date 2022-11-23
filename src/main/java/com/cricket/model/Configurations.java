package com.cricket.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Configurations")
@XmlAccessorType(XmlAccessType.FIELD)
public class Configurations {
	
	@XmlElement(name="filename")
	private String filename;
	
	@XmlElement(name="broadcaster")
	private String broadcaster;
	
	@XmlElement(name="sponsor")
	private String sponsor;
	
	@XmlElement(name="ipAddress")
	private String ipAddress;
	
	@XmlElement(name="portNumber")
	private int portNumber;
	
	@XmlElement(name="vizscene")
	private String vizscene;

	@XmlElement(name="statsOption")
	private String statsOption;
	
	public Configurations(String filename, String broadcaster, 
			String sponsor, String ipAddress, int portNumber, String vizscene) {
		super();
		this.filename = filename;
		this.broadcaster = broadcaster;
		this.sponsor = sponsor;
		this.ipAddress = ipAddress;
		this.portNumber = portNumber;
		this.vizscene = vizscene;
	}
	
	public String getStatsOption() {
		return statsOption;
	}

	public void setStatsOption(String statsOption) {
		this.statsOption = statsOption;
	}

	public Configurations() {
		super();
	}

	public String getVizscene() {
		return vizscene;
	}
	public void setVizscene(String vizscene) {
		this.vizscene = vizscene;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getBroadcaster() {
		return broadcaster;
	}
	public void setBroadcaster(String broadcaster) {
		this.broadcaster = broadcaster;
	}
	public String getSponsor() {
		return sponsor;
	}
	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}
	public int getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
}
