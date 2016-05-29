package com.xmlConfig.service.calculation;

import java.util.Map;

import com.xmlConfig.domain.Parameter;
import com.xmlConfig.exception.SolutionNotFoundException;

public interface Solver {

	public double solve(Map<Parameter, Double> equations, Parameter param) throws SolutionNotFoundException;
}
