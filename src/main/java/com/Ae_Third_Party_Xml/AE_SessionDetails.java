package com.Ae_Third_Party_Xml;

	import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

	@XmlRootElement(name="SessionDetails")
	@XmlAccessorType(XmlAccessType.FIELD)
	public class AE_SessionDetails {
		private Integer Runs,Wickets,Balls,Time;
		
		 @XmlAttribute(name = "Runs")
		public Integer getRuns() {
			return Runs;
		}

		public void setRuns(Integer runs) {
			Runs = runs;
		}
		 @XmlAttribute(name = "Wickets")
		public Integer getWickets() {
			return Wickets;
		}

		public void setWickets(Integer wicket) {
			Wickets = wicket;
		}
		 @XmlAttribute(name = "Balls")
		public Integer getBalls() {
			return Balls;
		}

		public void setBalls(Integer balls) {
			Balls = balls;
		}
		 @XmlAttribute(name = "Time")
		public Integer getTime() {
			return Time;
		}

		public void setTime(Integer time) {
			Time = time;
		}
		
		

	}
