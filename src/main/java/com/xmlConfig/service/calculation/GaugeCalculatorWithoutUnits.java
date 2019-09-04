package com.xmlConfig.service.calculation;


public class GaugeCalculatorWithoutUnits implements GaugeCalculator{

	@Override
	public double calculateGauge(String param) {
		String regex = "([kmgsPacNJKxyz]+[+-]?\\d*[/\\*]?)+";
		String gauge = param.replaceAll(regex, "");
		if(gauge.equals(""))
			return 1;
		
		return Double.valueOf(gauge);
	}

}
