package com.cricket.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Configurations")
@XmlAccessorType(XmlAccessType.FIELD)
public class Configuration {
	
	@XmlElement(name="primaryVariousOptions")
	private String primaryVariousOptions;
	
	@XmlElement(name="filename")
	private String filename;

	@XmlElement(name="secondaryFilename")
	private String secondaryFilename;
	
	@XmlElement(name="broadcaster")
	private String broadcaster;

	@XmlElement(name="secondaryBroadcaster")
	private String secondaryBroadcaster;
	
	@XmlElement(name="whichScene")
	private String whichScene;
	
	@XmlElement(name="qtIpAddress")
	private String qtIpAddress;
	
	@XmlElement(name="qtPortNumber")
	private int qtPortNumber;
	
	@XmlElement(name="qtScene")
	private String qtScene;

	@XmlElement(name="qtLanguage")
	private String qtLanguage;
	
	@XmlElement(name="primaryIpAddress")
	private String primaryIpAddress;
	
	@XmlElement(name="primaryPortNumber")
	private int primaryPortNumber;
	
	@XmlElement(name="primaryScene")
	private String primaryScene;

	@XmlElement(name="primaryLanguage")
	private String primaryLanguage;

	@XmlElement(name="secondaryIpAddress")
	private String secondaryIpAddress;
	
	@XmlElement(name="secondaryPortNumber")
	private int secondaryPortNumber;
	
	@XmlElement(name="secondaryScene")
	private String secondaryScene;

	@XmlElement(name="secondaryLanguage")
	private String secondaryLanguage;
	
	@XmlElement(name="tertiaryIpAddress")
	private String tertiaryIpAddress;
	
	@XmlElement(name="tertiaryPortNumber")
	private int tertiaryPortNumber;
	
	@XmlElement(name="tertiaryScene")
	private String tertiaryScene;

	@XmlElement(name="tertiaryLanguage")
	private String tertiaryLanguage;
	
	@XmlElement(name="speedUnit")
	private String speedUnit;
	
	@XmlElement(name="audio")
	private String audio;

	@XmlElement(name="qudich")
	private String qudich;

	@XmlElement(name="wagon")
	private String wagon;
	
	@XmlElement(name="preview")
	private String preview;
	
	public Configuration() {
		super();
	}

	public Configuration(String broadcaster) {
		super();
		this.broadcaster = broadcaster;
	}
	
	
	
	public Configuration(String filename, String broadcaster, String whichScene,
			String primaryIpAddress, int primaryPortNumber, String primaryScene, String primaryLanguage,
			String secondaryIpAddress, int secondaryPortNumber, String secondaryScene, String secondaryLanguage,
			String tertiaryIpAddress, int tertiaryPortNumber, String tertiaryScene, String tertiaryLanguage) {
		super();
		this.filename = filename;
		this.broadcaster = broadcaster;
		this.whichScene = whichScene;
		this.primaryIpAddress = primaryIpAddress;
		this.primaryPortNumber = primaryPortNumber;
		this.primaryScene = primaryScene;
		this.primaryLanguage = primaryLanguage;
		this.secondaryIpAddress = secondaryIpAddress;
		this.secondaryPortNumber = secondaryPortNumber;
		this.secondaryScene = secondaryScene;
		this.secondaryLanguage = secondaryLanguage;
		this.tertiaryIpAddress = tertiaryIpAddress;
		this.tertiaryPortNumber = tertiaryPortNumber;
		this.tertiaryScene = tertiaryScene;
		this.tertiaryLanguage = tertiaryLanguage;
	}

	public Configuration(String filename, String broadcaster, String speedUnit,String qudich,String wagon,String audio, String primaryIpAddress, 
			int primaryPortNumber, String primaryLanguage) {
		super();
		this.filename = filename;
		this.broadcaster = broadcaster;
		this.speedUnit = speedUnit;
		this.audio = audio;
		this.qudich = qudich;
		this.wagon = wagon;
		this.primaryIpAddress = primaryIpAddress;
		this.primaryPortNumber = primaryPortNumber;
		this.primaryLanguage = primaryLanguage;
		
	}

	public Configuration(String filename, String broadcaster,String secondaryBroadcaster,String qtIpAddress, 
			int qtPortNumber,String qtScene, String qtLanguage, String primaryIpAddress, int primaryPortNumber,
			String primaryScene, String primaryLanguage, String secondaryIpAddress, int secondaryPortNumber,
			String secondaryScene, String secondaryLanguage, String tertiaryIpAddress, int tertiaryPortNumber,
			String tertiaryScene, String tertiaryLanguage) {
		super();
		this.filename = filename;
		this.broadcaster = broadcaster;
		this.secondaryBroadcaster = secondaryBroadcaster;
		this.qtIpAddress = qtIpAddress;
		this.qtPortNumber = qtPortNumber;
		this.qtScene = qtScene;
		this.qtLanguage = qtLanguage;
		this.primaryIpAddress = primaryIpAddress;
		this.primaryPortNumber = primaryPortNumber;
		this.primaryScene = primaryScene;
		this.primaryLanguage = primaryLanguage;
		this.secondaryIpAddress = secondaryIpAddress;
		this.secondaryPortNumber = secondaryPortNumber;
		this.secondaryScene = secondaryScene;
		this.secondaryLanguage = secondaryLanguage;
		this.tertiaryIpAddress = tertiaryIpAddress;
		this.tertiaryPortNumber = tertiaryPortNumber;
		this.tertiaryScene = tertiaryScene;
		this.tertiaryLanguage = tertiaryLanguage;
	}

	
	public Configuration(String filename, String broadcaster, String secondaryBroadcaster, 
			String primaryIpAddress, int primaryPortNumber, String primaryLanguage,
			String qtIpAddress,int qtPortNumber, String primaryVariousOptions, String secondaryIpAddress, 
			int secondaryPortNumber,String secondaryLanguage, String preview) {
		super();
		this.filename = filename;
		this.broadcaster = broadcaster;
		this.secondaryBroadcaster = secondaryBroadcaster;
		this.primaryIpAddress = primaryIpAddress;
		this.primaryPortNumber = primaryPortNumber;
		this.primaryLanguage = primaryLanguage;
		this.qtIpAddress = qtIpAddress;
		this.qtPortNumber = qtPortNumber;
		this.primaryVariousOptions = primaryVariousOptions;
		this.secondaryIpAddress = secondaryIpAddress;
		this.secondaryPortNumber = secondaryPortNumber;
		this.secondaryLanguage = secondaryLanguage;
		this.preview = preview;
	}
	public Configuration(String selectedMatch, String select_broadcaster, String select_second_broadcaster,
			String primaryIpAddress, int primaryPortNumber, String primaryLanguage, String secondaryIpAddress,
			int secondaryPortNumber, String secondaryLanguage) {
			super();
			this.filename = selectedMatch;
			this.broadcaster = select_broadcaster;
			this.secondaryBroadcaster = select_second_broadcaster;
			this.primaryIpAddress = primaryIpAddress;
			this.primaryPortNumber = primaryPortNumber;
			this.primaryLanguage = primaryLanguage;
			this.secondaryIpAddress = secondaryIpAddress;
			this.secondaryPortNumber = secondaryPortNumber;
	}
	public Configuration(String filename, String broadcaster, String secondaryBroadcaster, String whichScene,
			String qtIpAddress, int qtPortNumber, String qtScene, String qtLanguage, String primaryIpAddress,
			int primaryPortNumber, String primaryScene, String primaryLanguage, String secondaryIpAddress,
			int secondaryPortNumber, String secondaryScene, String secondaryLanguage, String tertiaryIpAddress,
			int tertiaryPortNumber, String tertiaryScene, String tertiaryLanguage) {
		super();
		this.filename = filename;
		this.broadcaster = broadcaster;
		this.secondaryBroadcaster = secondaryBroadcaster;
		this.whichScene = whichScene;
		this.qtIpAddress = qtIpAddress;
		this.qtPortNumber = qtPortNumber;
		this.qtScene = qtScene;
		this.qtLanguage = qtLanguage;
		this.primaryIpAddress = primaryIpAddress;
		this.primaryPortNumber = primaryPortNumber;
		this.primaryScene = primaryScene;
		this.primaryLanguage = primaryLanguage;
		this.secondaryIpAddress = secondaryIpAddress;
		this.secondaryPortNumber = secondaryPortNumber;
		this.secondaryScene = secondaryScene;
		this.secondaryLanguage = secondaryLanguage;
		this.tertiaryIpAddress = tertiaryIpAddress;
		this.tertiaryPortNumber = tertiaryPortNumber;
		this.tertiaryScene = tertiaryScene;
		this.tertiaryLanguage = tertiaryLanguage;
	}
	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

	public String getPrimaryVariousOptions() {
		return primaryVariousOptions;
	}

	public void setPrimaryVariousOptions(String primaryVariousOptions) {
		this.primaryVariousOptions = primaryVariousOptions;
	}

	public String getQtIpAddress() {
		return qtIpAddress;
	}

	public void setQtIpAddress(String qtIpAddress) {
		this.qtIpAddress = qtIpAddress;
	}

	public int getQtPortNumber() {
		return qtPortNumber;
	}

	public void setQtPortNumber(int qtPortNumber) {
		this.qtPortNumber = qtPortNumber;
	}

	public String getQtScene() {
		return qtScene;
	}

	public void setQtScene(String qtScene) {
		this.qtScene = qtScene;
	}

	public String getQtLanguage() {
		return qtLanguage;
	}

	public void setQtLanguage(String qtLanguage) {
		this.qtLanguage = qtLanguage;
	}

	public String getSecondaryBroadcaster() {
		return secondaryBroadcaster;
	}

	public String getWhichScene() {
		return whichScene;
	}

	public void setWhichScene(String whichScene) {
		this.whichScene = whichScene;
	}

	public void setSecondaryBroadcaster(String secondaryBroadcaster) {
		this.secondaryBroadcaster = secondaryBroadcaster;
	}

	public String getSecondaryFilename() {
		return secondaryFilename;
	}

	public void setSecondaryFilename(String secondaryFilename) {
		this.secondaryFilename = secondaryFilename;
	}
		
	public String getPrimaryLanguage() {
		return primaryLanguage;
	}

	public void setPrimaryLanguage(String primaryLanguage) {
		this.primaryLanguage = primaryLanguage;
	}

	public String getSecondaryLanguage() {
		return secondaryLanguage;
	}

	public void setSecondaryLanguage(String secondaryLanguage) {
		this.secondaryLanguage = secondaryLanguage;
	}

	public String getTertiaryLanguage() {
		return tertiaryLanguage;
	}

	public void setTertiaryLanguage(String tertiaryLanguage) {
		this.tertiaryLanguage = tertiaryLanguage;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getBroadcaster() {
		return broadcaster;
	}

	public void setBroadcaster(String broadcaster) {
		this.broadcaster = broadcaster;
	}

	public String getPrimaryIpAddress() {
		return primaryIpAddress;
	}

	public void setPrimaryIpAddress(String primaryIpAddress) {
		this.primaryIpAddress = primaryIpAddress;
	}

	public int getPrimaryPortNumber() {
		return primaryPortNumber;
	}

	public void setPrimaryPortNumber(int primaryPortNumber) {
		this.primaryPortNumber = primaryPortNumber;
	}

	public String getPrimaryScene() {
		return primaryScene;
	}

	public void setPrimaryScene(String primaryScene) {
		this.primaryScene = primaryScene;
	}

	public String getSecondaryIpAddress() {
		return secondaryIpAddress;
	}

	public void setSecondaryIpAddress(String secondaryIpAddress) {
		this.secondaryIpAddress = secondaryIpAddress;
	}

	public int getSecondaryPortNumber() {
		return secondaryPortNumber;
	}

	public void setSecondaryPortNumber(int secondaryPortNumber) {
		this.secondaryPortNumber = secondaryPortNumber;
	}

	public String getSecondaryScene() {
		return secondaryScene;
	}

	public void setSecondaryScene(String secondaryScene) {
		this.secondaryScene = secondaryScene;
	}

	public String getTertiaryIpAddress() {
		return tertiaryIpAddress;
	}

	public void setTertiaryIpAddress(String tertiaryIpAddress) {
		this.tertiaryIpAddress = tertiaryIpAddress;
	}

	public int getTertiaryPortNumber() {
		return tertiaryPortNumber;
	}

	public void setTertiaryPortNumber(int tertiaryPortNumber) {
		this.tertiaryPortNumber = tertiaryPortNumber;
	}

	public String getTertiaryScene() {
		return tertiaryScene;
	}

	public void setTertiaryScene(String tertiaryScene) {
		this.tertiaryScene = tertiaryScene;
	}
	
	public String getSpeedUnit() {
		return speedUnit;
	}

	public void setSpeedUnit(String speedUnit) {
		this.speedUnit = speedUnit;
	}

	public String getAudio() {
		return audio;
	}

	public void setAudio(String audio) {
		this.audio = audio;
	}

	public String getQudich() {
		return qudich;
	}

	public void setQudich(String qudich) {
		this.qudich = qudich;
	}

	public String getWagon() {
		return wagon;
	}

	public void setWagon(String wagon) {
		this.wagon = wagon;
	}

	@Override
	public String toString() {
		return "Configuration [primaryVariousOptions=" + primaryVariousOptions + ", filename=" + filename
				+ ", secondaryFilename=" + secondaryFilename + ", broadcaster=" + broadcaster
				+ ", secondaryBroadcaster=" + secondaryBroadcaster + ", qtIpAddress=" + qtIpAddress + ", qtPortNumber="
				+ qtPortNumber + ", qtScene=" + qtScene + ", qtLanguage=" + qtLanguage + ", primaryIpAddress="
				+ primaryIpAddress + ", primaryPortNumber=" + primaryPortNumber + ", primaryScene=" + primaryScene
				+ ", primaryLanguage=" + primaryLanguage + ", secondaryIpAddress=" + secondaryIpAddress
				+ ", secondaryPortNumber=" + secondaryPortNumber + ", secondaryScene=" + secondaryScene
				+ ", secondaryLanguage=" + secondaryLanguage + ", tertiaryIpAddress=" + tertiaryIpAddress
				+ ", tertiaryPortNumber=" + tertiaryPortNumber + ", tertiaryScene=" + tertiaryScene
				+ ", tertiaryLanguage=" + tertiaryLanguage + ", speedUnit=" + speedUnit + ", audio=" + audio
				+ ", qudich=" + qudich + ", wagon=" + wagon + ", preview=" + preview + "]";
	}
}
