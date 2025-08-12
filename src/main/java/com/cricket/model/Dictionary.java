package com.cricket.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;

@Entity
@Table(name = "Dictionary")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dictionary  {

  @Id
  @Column(name = "DictionaryId")
  private int DictionaryId;
	
  @Column(name = "EnglishSentence")
  private String EnglishSentence;

  @Column(name = "HindiSentence")
  private String HindiSentence;

  @Column(name = "InsertNumbersBeforeThisHindiText")
  private String insertBeforeFirstHindiText;

  @Column(name = "InsertNumbersBeforeThisHindiText2")
  private String insertBeforeSecondHindiText;
  
  @Column(name = "TamilSentence")
  private String TamilSentence;

  @Column(name = "InsertNumbersBeforeThisTamilText")
  private String insertBeforeFirstTamilText;

  @Column(name = "InsertNumbersBeforeThisTamilText2")
  private String insertBeforeSecondTamilText;
  
  @Column(name = "TeluguSentence")
  private String TeluguSentence;

  @Column(name = "InsertNumbersBeforeThisTeluguText")
  private String insertBeforeFirstTeluguText;

  @Column(name = "InsertNumbersBeforeThisTeluguText2")
  private String insertBeforeSecondTeluguText;

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

public String getInsertBeforeFirstHindiText() {
	return insertBeforeFirstHindiText;
}

public void setInsertBeforeFirstHindiText(String insertBeforeFirstHindiText) {
	this.insertBeforeFirstHindiText = insertBeforeFirstHindiText;
}

public String getInsertBeforeSecondHindiText() {
	return insertBeforeSecondHindiText;
}

public void setInsertBeforeSecondHindiText(String insertBeforeSecondHindiText) {
	this.insertBeforeSecondHindiText = insertBeforeSecondHindiText;
}

public String getTamilSentence() {
	return TamilSentence;
}

public void setTamilSentence(String tamilSentence) {
	TamilSentence = tamilSentence;
}

public String getInsertBeforeFirstTamilText() {
	return insertBeforeFirstTamilText;
}

public void setInsertBeforeFirstTamilText(String insertBeforeFirstTamilText) {
	this.insertBeforeFirstTamilText = insertBeforeFirstTamilText;
}

public String getInsertBeforeSecondTamilText() {
	return insertBeforeSecondTamilText;
}

public void setInsertBeforeSecondTamilText(String insertBeforeSecondTamilText) {
	this.insertBeforeSecondTamilText = insertBeforeSecondTamilText;
}

public String getTeluguSentence() {
	return TeluguSentence;
}

public void setTeluguSentence(String teluguSentence) {
	TeluguSentence = teluguSentence;
}

public String getInsertBeforeFirstTeluguText() {
	return insertBeforeFirstTeluguText;
}

public void setInsertBeforeFirstTeluguText(String insertBeforeFirstTeluguText) {
	this.insertBeforeFirstTeluguText = insertBeforeFirstTeluguText;
}

public String getInsertBeforeSecondTeluguText() {
	return insertBeforeSecondTeluguText;
}

public void setInsertBeforeSecondTeluguText(String insertBeforeSecondTeluguText) {
	this.insertBeforeSecondTeluguText = insertBeforeSecondTeluguText;
}
  
}