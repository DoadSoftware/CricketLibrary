package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForeignLanguageData {
	
	private String EnglishText;
	private String HindiText;
	private String TamilText;
	private String TeluguText;
	
	public ForeignLanguageData(String englishText, String hindiText, String tamilText, String teluguText) {
		super();
		EnglishText = englishText;
		HindiText = hindiText;
		TamilText = tamilText;
		TeluguText = teluguText;
	}
	public ForeignLanguageData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getEnglishText() {
		return EnglishText;
	}
	public void setEnglishText(String englishText) {
		EnglishText = englishText;
	}
	public String getHindiText() {
		return HindiText;
	}
	public void setHindiText(String hindiText) {
		HindiText = hindiText;
	}
	public String getTamilText() {
		return TamilText;
	}
	public void setTamilText(String tamilText) {
		TamilText = tamilText;
	}
	public String getTeluguText() {
		return TeluguText;
	}
	public void setTeluguText(String teluguText) {
		TeluguText = teluguText;
	}
	@Override
	public String toString() {
		return "ForiegnLanguageData [EnglishText=" + EnglishText + ", HindiText=" + HindiText + ", TamilText="
				+ TamilText + ", TeluguText=" + TeluguText + "]";
	}
}
