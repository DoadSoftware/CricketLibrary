package com.cricket.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldersFormation {	
	
	    @JsonProperty("fielderId")
	    private int fielderId;

	    @JsonProperty("leftLocation")
	    private int leftLocation;

	    @JsonProperty("topLocation")
	    private int topLocation;

	    @JsonProperty("fielderhighlight")
	    private String fielderhighlight;

	    // Add getters and setters for the fields

	    public int getFielderId() {
	        return fielderId;
	    }

	    public void setFielderId(int fielderId) {
	        this.fielderId = fielderId;
	    }

	    public int getLeftLocation() {
	        return leftLocation;
	    }

	    public void setLeftLocation(int leftLocation) {
	        this.leftLocation = leftLocation;
	    }

	    public int getTopLocation() {
	        return topLocation;
	    }

	    public void setTopLocation(int topLocation) {
	        this.topLocation = topLocation;
	    }

	    public String getFielderhighlight() {
	        return fielderhighlight;
	    }

	    public void setFielderhighlight(String fielderhighlight) {
	        this.fielderhighlight = fielderhighlight;
	        
	    }

		@Override
		public String toString() {
			return "FielderFormation [fielderId=" + fielderId + ", leftLocation=" + leftLocation + ", topLocation="
					+ topLocation + ", fielderhighlight=" + fielderhighlight + "]";
		}

		public FieldersFormation(int fielderId, int leftLocation, int topLocation, String fielderhighlight) {
			super();
			this.fielderId = fielderId;
			this.leftLocation = leftLocation;
			this.topLocation = topLocation;
			this.fielderhighlight = fielderhighlight;
		}

		public FieldersFormation() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		
	}