package com.cricket.model;

public class ForiegnLanguageData {
	private String EnglishText;
	private String HindiText;
	private String TamilText;
	private String TeluguText;
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
