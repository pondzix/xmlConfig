package com.xmlConfig.service;

import org.w3c.dom.Node;

import com.xmlConfig.domain.Command;
import com.xmlConfig.domain.XmlFileAdapter;
import com.xmlConfig.exception.IllegalFileModification;

public class UnitsUpdateService implements UpdateService{

   private XmlFileAdapter fileModel;
	
	public UnitsUpdateService(XmlFileAdapter fileModel) {
		this.fileModel = fileModel;
	}
	
	@Override
	public void update(Command command) throws IllegalFileModification {
		
		switch(command.getActionType()){
			case CHANGE_UNIT_GAUGE:
				
				break;
			case CHANGE_UNIT_NAME:
				
				break;
			case CHANGE_UNIT_VALUE:
				
				break;
			default:
				break;	
		}
	}

	@Override
	public void addElement(Node parent) throws IllegalFileModification {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAttribute(Node parent) throws IllegalFileModification {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(Command command) {
		// TODO Auto-generated method stub
		
	}

}
