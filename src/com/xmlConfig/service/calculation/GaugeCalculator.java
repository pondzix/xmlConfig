package com.xmlConfig.service.calculation;

import com.xmlConfig.exception.InvalidInputParameterException;
import com.xmlConfig.exception.SolutionNotFoundException;


public interface GaugeCalculator {

	public double calculateGauge(String param) throws InvalidInputParameterException, SolutionNotFoundException;
}
