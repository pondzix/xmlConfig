package com.xmlConfig.service;

import org.w3c.dom.Node;

import com.xmlConfig.domain.Command;
import com.xmlConfig.exception.IllegalFileModification;

public interface UpdateService {

	public void update(Command command) throws IllegalFileModification;
	
	public void addElement(Node parent) throws IllegalFileModification;
	
	public void addAttribute(Node parent) throws IllegalFileModification;
	
	public void remove(Command command);
	
}
