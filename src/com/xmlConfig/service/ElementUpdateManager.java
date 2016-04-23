package com.xmlConfig.service;


import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import com.xmlConfig.domain.ActionType;
import com.xmlConfig.domain.Command;
import com.xmlConfig.domain.XmlFileAdapter;
import com.xmlConfig.exception.IllegalFileModification;

public class ElementUpdateManager implements UpdateManager{

	private XmlFileAdapter fileModel;
	
	public ElementUpdateManager(XmlFileAdapter fileModel) {
		this.fileModel = fileModel;
	}
	
	@Override
	public void update(Command item) throws IllegalFileModification {
		ActionType type = item.getActionType();
		Element element, newElement;
		
		switch(type){	
			case CHANGE_VALUE:
				throw new IllegalFileModification();
				
			case CHANGE_NAME:
				element = (Element) fileModel.getNodeById(item.getItemId());
				fileModel.getDocument().renameNode(element, null, item.getNewValue());	
				break;
				
			case ADD_ELEMENT:
				element = (Element) fileModel.getNodeById(item.getItemId());
				newElement = fileModel.getDocument().createElement("NEW");
				element.appendChild(newElement);
				fileModel.addItem(newElement);
				break;
				
			case ADD_ATTRIBUTE:
				element = (Element) fileModel.getNodeById(item.getItemId());
				Attr attribute = fileModel.getDocument().createAttribute("NEW");
				attribute.setValue("NEW");
				element.setAttributeNode(attribute);
				fileModel.addItem(attribute);				
				break;						
		}
	}
}
