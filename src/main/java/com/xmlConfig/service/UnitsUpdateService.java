package com.xmlConfig.service;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.xmlConfig.domain.Command;
import com.xmlConfig.domain.FileAdapter;
import com.xmlConfig.exception.IllegalFileModification;

public class UnitsUpdateService implements UpdateService{

   private FileAdapter fileModel;
	
	public UnitsUpdateService(FileAdapter fileModel) {
		this.fileModel = fileModel;
	}
	
	@Override
	public void update(Command command) throws IllegalFileModification {
		Element owner;
		
		switch(command.getActionType()){
			case CHANGE_UNIT_GAUGE:
				owner = (Element) fileModel.getNodeById(command.getItemId());
				owner.removeAttribute("gauge");
				owner.setAttribute("gauge", command.getNewValue());
				fileModel.updateItem(command.getItemId() + 1, owner.getAttributeNode("gauge"));
				break;
			case CHANGE_UNIT_NAME:
				//TODO
				break;
			case CHANGE_UNIT_VALUE:
				//TODO
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
