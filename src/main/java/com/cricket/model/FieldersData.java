package com.cricket.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldersData{
	 @JsonProperty("Checkbox")
	 private boolean Checkbox;
	 
	 @JsonProperty("style")
	 private  String style ;
			
	 @JsonProperty("fielders")
	 private  List<FieldersFormation> Fielders ;

	public boolean isCheckbox() {
		return Checkbox;
	}

	public void setCheckbox(boolean checkbox) {
		Checkbox = checkbox;
	}

	public List<FieldersFormation> getFielders() {
		return Fielders;
	}

	public void setFielders(List<FieldersFormation> fielders) {
		Fielders = fielders;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	public FieldersData(boolean checkbox, String style, List<FieldersFormation> fielders) {
		super();
		Checkbox = checkbox;
		this.style = style;
		Fielders = fielders;
	}

	public FieldersData() {
		super();
	}

	@Override
	public String toString() {
		return "FieldersData [Checkbox=" + Checkbox + ", style=" + style + ", Fielders=" + Fielders + "]";
	}
	
	 
}