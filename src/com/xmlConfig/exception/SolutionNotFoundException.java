package com.xmlConfig.exception;

public class SolutionNotFoundException extends Exception{

	private static final long serialVersionUID = 1L;

	public SolutionNotFoundException() {
		super("Can not find solution");
	}
}
