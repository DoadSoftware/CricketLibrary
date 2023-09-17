package com.Ae_Third_Party_Xml;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="Player")

public class AE_Player_Info {
	private Integer position;
    private Integer id;
    private String firstName;
    private String surname;
    private String initialName;
    private String captain;
    private String dateOfBirth;
    private String batStyle;
    private String bowlStyle;
    private String role,Keeper;
    
    @XmlAttribute(name = "Position")
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	@XmlAttribute(name = "ID")
	public Integer getID() {
		return id;
	}
	public void setID(Integer iD) {
		id = iD;
	}
	@XmlAttribute(name = "FirstName")
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	@XmlAttribute(name = "Surname")
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	@XmlAttribute(name = "InitialName")
	public String getInitialName() {
		return initialName;
	}
	public void setInitialName(String initialName) {
		this.initialName = initialName;
	}
	@XmlAttribute(name = "Captain")
	public String getCaptain() {
		return captain;
	}
	public void setCaptain(String captain) {
		this.captain = captain;
	}
	@XmlAttribute(name = "DateOfBirth")
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	@XmlAttribute(name = "BatStyle")
	public String getBatStyle() {
		return batStyle;
	}
	public void setBatStyle(String batStyle) {
		this.batStyle = batStyle;
	}
	@XmlAttribute(name = "BowlStyle")
	public String getBowlStyle() {
		return bowlStyle;
	}
	public void setBowlStyle(String bowlStyle) {
		this.bowlStyle = bowlStyle;
	}
	@XmlAttribute(name = "Role")
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	@XmlAttribute(name = "Keeper")
	public String getKeeper() {
		return Keeper;
	}
	public void setKeeper(String keeper) {
		Keeper = keeper;
	}

   
}