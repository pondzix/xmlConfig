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
	private NodeValidation nodeValidation = new NodeValidation();
	
	public AttributeUpdateService(XmlFileAdapter fileModel) {
		this.fileModel = fileModel;
	}
	
	@Override
	public void update(Command command) throws IllegalFileModification{
		ActionType type = command.getActionType();		
		Attr attribute = (Attr) fileModel.getNodeById(command.getItemId());
		Element owner = attribute.getOwnerElement();
		
		
		switch(type){	
			case CHANGE_VALUE:
				owner.removeAttributeNode(attribute);
				owner.setAttribute(attribute.getName(), command.getNewValue());	
				fileModel.updateItem(command.getItemId(), owner.getAttributeNode(attribute.getName()));
				break;			
			case CHANGE_NAME:							
				owner.removeAttributeNode(attribute);
				owner.setAttribute(command.getNewValue(), attribute.getValue());
				fileModel.updateItem(command.getItemId(), owner.getAttributeNode(command.getNewValue()));	
				
				if(!nodeValidation.isValidAttribute(owner, command.getNewValue()))
					throw new IllegalFileModification("Invalid name for attribute");	
				if(owner.getAttributeNode(command.getNewValue()) != null)
					throw new IllegalFileModification("Attribute already exists");	
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
