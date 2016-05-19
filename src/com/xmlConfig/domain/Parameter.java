package com.xmlConfig.domain;

import java.util.Arrays;

public class Parameter {

	
	private double value;
	private double[] unit;

	public Parameter(double value, double[] unit){
		this.setValue(value);
		this.setUnit(unit);
	}

	public Parameter() {
		
	}

	public double[] getUnit() {
		return unit;
	}

	public void setUnit(double[] unit) {
		this.unit = unit;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	@Override
	public String toString(){
		String val = "v:" + value +", unit:" + Arrays.toString(unit);
		return val;	
	}

}
