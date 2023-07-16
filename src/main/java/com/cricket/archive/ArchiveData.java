package com.cricket.archive;

public class ArchiveData{

  private String label;
  private String url;
  
public String getLabel() {
	return label;
}
public void setLabel(String label) {
	this.label = label;
}
public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}
public ArchiveData() {
	super();
	// TODO Auto-generated constructor stub
}
public ArchiveData(String label, String url) {
	super();
	this.label = label;
	this.url = url;
}
  
  
}