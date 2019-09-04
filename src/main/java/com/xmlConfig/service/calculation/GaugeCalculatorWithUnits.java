package com.xmlConfig.service.calculation;

import java.util.Map;

import com.xmlConfig.domain.Parameter;
import com.xmlConfig.exception.InvalidInputParameterException;
import com.xmlConfig.exception.SolutionNotFoundException;
import com.xmlConfig.service.UnitUtils;


public class GaugeCalculatorWithUnits implements GaugeCalculator{

	private Solver solver = new SolverImpl();
	private Map<Parameter, Double> equations;
	
	
	public GaugeCalculatorWithUnits(Map<Parameter, Double> equations) {
		this.equations = equations;
	}
	
	@Override
	public double calculateGauge(String param) throws InvalidInputParameterException, SolutionNotFoundException {
		Parameter p = UnitUtils.parseStringToParameter(param);
		return solver.solve(equations, p);
	}

}
