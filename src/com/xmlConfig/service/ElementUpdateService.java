package com.xmlConfig.service;


import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


import com.xmlConfig.domain.ActionType;
import com.xmlConfig.domain.Command;
import com.xmlConfig.domain.XmlFileAdapter;
import com.xmlConfig.exception.IllegalFileModification;

public class ElementUpdateService implements UpdateService{

	private XmlFileAdapter fileModel;
	
	public ElementUpdateService(XmlFileAdapter fileModel) {
		this.fileModel = fileModel;
	}
	
	@Override
	public void update(Command command) throws IllegalFileModification {
		ActionType type = command.getActionType();
		Element element;
		
		switch(type){	
			case CHANGE_VALUE:
				throw new IllegalFileModification();
				
			case CHANGE_NAME:
				element = (Element) fileModel.getNodeById(command.getItemId());
				fileModel.getDocument().renameNode(element, null, command.getNewValue());	
				break;				
		}
	}

	@Override
	public void addElement(Node parent) {
		Element newElement = fileModel.getDocument().createElement("NEW");
		parent.appendChild(newElement);
		fileModel.addItem(newElement);		
	}

	@Override
	public void addAttribute(Node parent) {
		Element element = (Element) parent;
		Attr attribute = fileModel.getDocument().createAttribute("NEW");
		element.setAttributeNode(attribute);
		fileModel.addItem(attribute);			
	}

	@Override
	public void remove(Command command) {
		Element element = (Element) fileModel.getNodeById(command.getItemId());
		element.getParentNode().removeChild(element);	
	}
}
