package com.xmlConfig.domain;

import java.util.Arrays;

public class Parameter {

	
	private double value;
	private int[] unit;

	public Parameter(double value, int[] unit){
		this.setValue(value);
		this.setUnit(unit);
	}

	public Parameter() {
		
	}

	public int[] getUnit() {
		return unit;
	}

	public void setUnit(int[] unit) {
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
