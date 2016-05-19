package com.xmlConfig.service;

import java.util.HashMap;
import java.util.Map;

import com.xmlConfig.domain.Parameter;
import com.xmlConfig.exception.InvalidInputParameterException;
import com.xmlConfig.exception.SolutionNotFoundException;

public class UnitService {

	private Map<Parameter, Double> equations = new HashMap<>();
	private UnitUtils utils = new UnitUtils();
	private Solver solver = new SolverImpl();
	
	public void addEquation(String param, String gauge) throws InvalidInputParameterException{
		Parameter p = utils.parseStringToParameter(param);
		equations.put(p, Double.valueOf(gauge)/ p.getValue());
	}
	
	
	public double calculateGauge(String param) throws SolutionNotFoundException, InvalidInputParameterException{
		Parameter p = utils.parseStringToParameter(param);
		return solver.solve(equations, p);
	}
	
	public void printEquations(){
		for(Map.Entry<Parameter, Double> e: equations.entrySet())
			System.out.println(e.getKey() + "---" + e.getValue());	
	}
}
