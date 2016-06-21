package com.xmlConfig.service;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.xmlConfig.domain.ActionType;
import com.xmlConfig.domain.Command;
import com.xmlConfig.domain.XmlFileAdapter;
import com.xmlConfig.exception.IllegalFileModification;

public class AttributeUpdateService implements UpdateService{

	private XmlFileAdapter fileModel;
	
	public AttributeUpdateService(XmlFileAdapter fileModel) {
		this.fileModel = fileModel;
	}
	
	@Override
	public void update(Command command){
		ActionType type = command.getActionType();		
		Attr attribute = (Attr) fileModel.getNodeById(command.getItemId());
		Element element = attribute.getOwnerElement();
		element.removeAttributeNode(attribute);
		
		switch(type){	
			case CHANGE_VALUE:
				element.setAttribute(attribute.getName(), command.getNewValue());	
				fileModel.updateItem(command.getItemId(), element.getAttributeNode(attribute.getName()));
				break;			
			case CHANGE_NAME:
				element.setAttribute(command.getNewValue(), attribute.getValue());
				fileModel.updateItem(command.getItemId(), element.getAttributeNode(command.getNewValue()));			
				break;
			default:
				break;		
		}				
	}

	@Override
	public void addElement(Node parent) throws IllegalFileModification {
		throw new IllegalFileModification("Can not add child element to attribute");
		
	}

	@Override
	public void addAttribute(Node parent) throws IllegalFileModification {
		throw new IllegalFileModification("Can not add attribute to attribute");
		
	}

	@Override
	public void remove(Command command) {
		Attr attribute = (Attr) fileModel.getNodeById(command.getItemId());
		Element element = attribute.getOwnerElement();
		element.removeAttributeNode(attribute);	
	}
}
