package com.Ae_Third_Party_Xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Projection ")
public class AE_Projection {
	@XmlAttribute(name="ProjectionAtCurrent")
	private Integer ProjectionAtCurrent;
	@XmlAttribute(name="ProjectionAt6PerOver")
	private Integer ProjectionAt6PerOver;
	@XmlAttribute(name="ProjectionAt7PerOver")
	private Integer ProjectionAt7PerOver;
	@XmlAttribute(name="ProjectionAt8PerOver")
	private Integer ProjectionAt8PerOver;
	@XmlAttribute(name="ProjectionAt9PerOver")
	private Integer ProjectionAt9PerOver;
	public Integer getProjectionAtCurrent() {
		return ProjectionAtCurrent;
	}
	public void setProjectionAtCurrent(Integer projectionAtCurrent) {
		ProjectionAtCurrent = projectionAtCurrent;
	}
	public Integer getProjectionAt6PerOver() {
		return ProjectionAt6PerOver;
	}
	public void setProjectionAt6PerOver(Integer projectionAt6PerOver) {
		ProjectionAt6PerOver = projectionAt6PerOver;
	}
	public Integer getProjectionAt7PerOver() {
		return ProjectionAt7PerOver;
	}
	public void setProjectionAt7PerOver(Integer projectionAt7PerOver) {
		ProjectionAt7PerOver = projectionAt7PerOver;
	}
	public Integer getProjectionAt8PerOver() {
		return ProjectionAt8PerOver;
	}
	public void setProjectionAt8PerOver(Integer projectionAt8PerOver) {
		ProjectionAt8PerOver = projectionAt8PerOver;
	}
	public Integer getProjectionAt9PerOver() {
		return ProjectionAt9PerOver;
	}
	public void setProjectionAt9PerOver(Integer projectionAt9PerOver) {
		ProjectionAt9PerOver = projectionAt9PerOver;
	}
	

}
