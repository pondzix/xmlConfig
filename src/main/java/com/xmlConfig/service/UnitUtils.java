package com.xmlConfig.service;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xmlConfig.domain.Parameter;
import com.xmlConfig.exception.InvalidInputParameterException;

public class UnitUtils {

	public static String[] basicUnitsNames = {
			"mm", "cm", "ms", "mg", "m",
			"km", "s", "ms","ks","g",
			"Pa", "N", "J", "K", "x",
			"y", "z"};
	public static Map<String, Parameter> basicUnits = new HashMap<>();
	public static String reg;
	public static String mainRegex;
	public static String numberRegex = "([-+]?[0-9]*(\\.[0-9]*)?([eE][+-]?[0-9]+)?)?";
	
	static {
		basicUnits.put("mm", new Parameter(1e-3, new int[]{1, 0, 0, 0, 0, 0, 0}));
		basicUnits.put("cm", new Parameter(1e-2, new int[]{1, 0, 0, 0, 0, 0, 0}));
		basicUnits.put("ms", new Parameter(1e-3, new int[]{0, 1, 0, 0, 0, 0, 0}));
		basicUnits.put("mg", new Parameter(1e-6, new int[]{0, 0, 1, 0, 0, 0, 0}));
		basicUnits.put("m",  new Parameter(1,    new int[]{1, 0, 0, 0, 0, 0, 0}));
		basicUnits.put("km", new Parameter(1e3,  new int[]{1, 0, 0, 0, 0, 0, 0}));
		basicUnits.put("s",  new Parameter(1,    new int[]{0, 1, 0, 0, 0, 0, 0}));
		basicUnits.put("ms", new Parameter(1e-3, new int[]{0, 1, 0, 0, 0, 0, 0}));
		basicUnits.put("kg", new Parameter(1,    new int[]{0, 0, 1, 0, 0, 0, 0}));
		basicUnits.put("g",  new Parameter(1e-3, new int[]{0, 0, 1, 0, 0, 0, 0}));
		basicUnits.put("Pa", new Parameter(1,    new int[]{-1, -2, 1, 0, 0, 0, 0}));
		basicUnits.put("N",  new Parameter(1,    new int[]{1, -2, 1, 0, 0, 0, 0}));
		basicUnits.put("J",  new Parameter(1,    new int[]{2, -2, 1, 0, 0, 0, 0}));
		basicUnits.put("K",  new Parameter(1,    new int[]{0, 0, 0, 1, 0, 0, 0}));
		basicUnits.put("x",  new Parameter(1,    new int[]{0, 0, 0, 0, 1, 0, 0}));
		basicUnits.put("y",  new Parameter(1,    new int[]{0, 0, 0, 0, 0, 1, 0}));
		basicUnits.put("z",  new Parameter(1,    new int[]{0, 0, 0, 0, 0, 0, 1}));
		
		createRegex();		
	}
	
	public static void createRegex() {
		StringBuilder sb  = new StringBuilder();
		
		sb.append("(");
		for(String s: basicUnitsNames)
			sb.append(s + "[+-]?[0-9]*|");
	
		sb.append(")");
		reg = sb.toString();
		mainRegex = "([-+]?[0-9]*(\\.[0-9]*)?([eE][+-]?[0-9]+)?)([mksgcPaNJKxyz]+[+-]?\\d*[/\\*]?)+";
		numberRegex = "([-+]?[0-9]*(\\.[0-9]*)?([eE][+-]?[0-9]+)?)?";
	}

	
	public static Parameter parseStringToParameter(String unit) throws InvalidInputParameterException{
		
		Parameter param = new Parameter();
		Pattern p = Pattern.compile(mainRegex);
		Matcher m = p.matcher(unit);
		double value;
		if(!m.find())
			throw new InvalidInputParameterException();
		
		if(m.group(1).equals(""))
			value = 1;
		else
			value = Double.valueOf(m.group(1));

		Parameter temp = getParam(unit.replace(m.group(1), ""));
		param.setValue(value * temp.getValue());
		param.setUnit(temp.getUnit());	
		return param;
	}
	
	private static Parameter getParam(String unit){
		Parameter param = new Parameter();
		int[] paramUnit = new int[7];
		double paramValue;
			
		if(unit.contains("/")){
			String num  = unit.substring(0, unit.indexOf('/'));	
			Parameter den = getParam(unit.substring(unit.indexOf('/') + 1));
			paramValue = convertMultiUnitsToSingle(num, paramUnit);
			subtractArrays(paramUnit, den.getUnit());
			param.setValue(paramValue / den.getValue());
		}else{
			paramValue = convertMultiUnitsToSingle(unit, paramUnit);
			param.setValue(paramValue);				
		}	
		param.setUnit(paramUnit);
		return param;	 
	}
	
	private static double convertMultiUnitsToSingle(String unit, int[] paramUnit){
		int[] paramUnitTemp = new int[7];
		double paramValue = 1;
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(unit);
	
		 while(m.find() && !m.group().equals("")){
			 String unitWithPow = m.group();
			 String pow = unitWithPow.replaceAll("[^0-9-]", "");
			 String unitName = unitWithPow.replaceAll(pow, "");
			 copyArray(basicUnits.get(unitName).getUnit(), paramUnitTemp);	
			 if(!pow.equals("")){
				 paramValue *= Math.pow(basicUnits.get(unitName).getValue(), Double.valueOf(pow));
				 multiplyArray(Integer.valueOf(pow), paramUnitTemp);
			 }					 		 
			 addArrays(paramUnit, paramUnitTemp);	
		 }
		 
		 return paramValue;
	}

	private static void copyArray(int[] from, int[] to){
		for(int i = 0; i < to.length; i++)
			to[i] = from[i];
	}
	private static void multiplyArray(int value, int[] unit) {
		for(int i = 0; i < unit.length; i++)
			unit[i] *= value;
	}
	
	private static void addArrays(int[] unit, int[] unitTemp){
		for(int i = 0; i < unit.length; i++)
			unit[i] += unitTemp[i];	
	}
	
	private static void subtractArrays(int[] unit, int[] unitTemp){
		for(int i = 0; i < unit.length; i++)
			unit[i] -= unitTemp[i];
	}
}
