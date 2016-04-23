package com.xmlConfig.service;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import com.xmlConfig.domain.ActionType;
import com.xmlConfig.domain.Command;
import com.xmlConfig.domain.XmlFileAdapter;
import com.xmlConfig.exception.IllegalFileModification;

public class AttributeUpdateManager implements UpdateManager{

	private XmlFileAdapter fileModel;
	
	public AttributeUpdateManager(XmlFileAdapter fileModel) {
		this.fileModel = fileModel;
	}
	
	@Override
	public void update(Command item) throws IllegalFileModification {
		ActionType type = item.getActionType();
		if(type == ActionType.ADD_ATTRIBUTE || type == ActionType.ADD_ELEMENT)
			throw new IllegalFileModification();
			
		Attr attribute = (Attr) fileModel.getNodeById(item.getItemId());
		Element element = (Element) fileModel.getNodeById(item.getParentItemId());
		element.removeAttributeNode(attribute);
		fileModel.removeItem(item.getItemId());
		
		switch(type){	
			case CHANGE_VALUE:
				element.setAttribute(attribute.getName(), item.getNewValue());	
				fileModel.addItem(item.getItemId(), element.getAttributeNode(attribute.getName()));
				break;
				
			case CHANGE_NAME:
				element.setAttribute(item.getNewValue(), attribute.getValue());
				fileModel.addItem(item.getItemId(), element.getAttributeNode(item.getNewValue()));			
				break;
			default:
				break;
				
		}				
	}
}
