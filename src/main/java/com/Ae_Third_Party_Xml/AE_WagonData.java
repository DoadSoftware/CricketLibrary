package com.Ae_Third_Party_Xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="WB ")
public class AE_WagonData {
	@XmlAttribute(name="ID")
	private Integer Ident;
	@XmlAttribute(name="X")
	private Integer X;
	@XmlAttribute(name="Y")
	private Integer Y;
	@XmlAttribute(name="S")
	private Integer S;
	@XmlAttribute(name="H")
	private Integer H;
	@XmlAttribute(name="B")
	private Integer B;
	@XmlAttribute(name="R")
	private Integer R;
	public Integer getID() {
		return Ident;
	}
	public void setID(Integer id) {
		Ident = id;
	}
	public Integer getX() {
		return X;
	}
	public void setX(Integer x) {
		X = x;
	}
	public Integer getY() {
		return Y;
	}
	public void setY(Integer y) {
		Y = y;
	}
	public Integer getS() {
		return S;
	}
	public void setS(Integer s) {
		S = s;
	}
	public Integer getH() {
		return H;
	}
	public void setH(Integer h) {
		H = h;
	}
	public Integer getB() {
		return B;
	}
	public void setB(Integer b) {
		B = b;
	}
	public Integer getIdent() {
		return Ident;
	}
	public void setIdent(Integer ident) {
		Ident = ident;
	}
	public Integer getR() {
		return R;
	}
	public void setR(Integer r) {
		R = r;
	}
	@Override
	public String toString() {
		return "AE_WagonData [Ident=" + Ident + ", X=" + X + ", Y=" + Y + ", S=" + S + ", H=" + H + ", B=" + B + ", R="
				+ R + "]";
	}
	

}
