package com.cricket.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "Dictionary")
public class Dictionary  {

  @Id
  @Column(name = "DictionaryId")
  private int DictionaryId;
	
  @Column(name = "EnglishSentence")
  private String EnglishSentence;

  @Column(name = "HindiSentence")
  private String HindiSentence;
  
  @Column(name = "TamilSentence")
  private String TamilSentence;
  
  @Column(name = "TeluguSentence")
  private String TeluguSentence;

public int getDictionaryId() {
	return DictionaryId;
}

public void setDictionaryId(int dictionaryId) {
	DictionaryId = dictionaryId;
}

public String getEnglishSentence() {
	return EnglishSentence;
}

public void setEnglishSentence(String englishSentence) {
	EnglishSentence = englishSentence;
}

public String getHindiSentence() {
	return HindiSentence;
}

public void setHindiSentence(String hindiSentence) {
	HindiSentence = hindiSentence;
}

public String getTamilSentence() {
	return TamilSentence;
}

public void setTamilSentence(String tamilSentence) {
	TamilSentence = tamilSentence;
}

public String getTeluguSentence() {
	return TeluguSentence;
}

public void setTeluguSentence(String teluguSentence) {
	TeluguSentence = teluguSentence;
}

@Override
public String toString() {
	return "Dictionary [DictionaryId=" + DictionaryId + ", EnglishSentence=" + EnglishSentence + ", HindiSentence="
			+ HindiSentence + ", TamilSentence=" + TamilSentence + ", TeluguSentence=" + TeluguSentence + "]";
}

}