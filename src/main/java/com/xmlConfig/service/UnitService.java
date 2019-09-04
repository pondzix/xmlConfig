package com.xmlConfig.service;

import java.util.HashMap;
import java.util.Map;

import com.xmlConfig.domain.Parameter;
import com.xmlConfig.exception.InvalidInputParameterException;
import com.xmlConfig.exception.SolutionNotFoundException;
import com.xmlConfig.service.calculation.GaugeCalculator;
import com.xmlConfig.service.calculation.GaugeCalculatorWithUnits;
import com.xmlConfig.service.calculation.GaugeCalculatorWithoutUnits;

public class UnitService {
	
	private FileService fileService;
	private GaugeCalculator gaugeCalculator;
	
	
	public void setFileService(FileService fileService) throws InvalidInputParameterException {
		this.fileService = fileService;
		setGaugeCalculator();
	}
	
	public String calculateGauge(String param) throws SolutionNotFoundException, InvalidInputParameterException{
		if(param.matches(UnitUtils.mainRegex)){
			//gauge
			return Double.toString(gaugeCalculator.calculateGauge(param));
		}
		else if(param.matches(UnitUtils.numberRegex))
			return param;
		else
			return "";
	}

	public Map<Integer, String> getGauges() throws SolutionNotFoundException, InvalidInputParameterException {
		Map<Integer, String> gaugeMap = new HashMap<>();
		for(Map.Entry<Integer, String> entry: fileService.getValuesWithGauge().entrySet())
			gaugeMap.put(entry.getKey(), calculateGauge(entry.getValue()));
					
		return gaugeMap;
	}
	
	private void setGaugeCalculator() throws InvalidInputParameterException {
		if(fileService.getFileModel().hasUnits())
			gaugeCalculator = new GaugeCalculatorWithUnits(getEquations());
		else
			gaugeCalculator = new GaugeCalculatorWithoutUnits();
	}
	
	private Map<Parameter, Double> getEquations() throws InvalidInputParameterException{
		Map<Parameter, Double> eq = new HashMap<>();
		for(Map.Entry<String, String> entry: fileService.getFileModel().getEquations().entrySet()){
			Parameter p = UnitUtils.parseStringToParameter(entry.getKey());
			eq.put(p, Double.valueOf(entry.getValue()) / p.getValue());
		}
		
		return eq;
		
	}

	public void updateEquations() throws InvalidInputParameterException {
		gaugeCalculator = new GaugeCalculatorWithUnits(getEquations());	
	}
		
	
}
