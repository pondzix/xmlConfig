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
	private NodeValidation nodeValidation = new NodeValidation();
	
	public ElementUpdateService(XmlFileAdapter fileModel) {
		this.fileModel = fileModel;
	}
	
	@Override
	public void update(Command command) throws IllegalFileModification {
		ActionType type = command.getActionType();
		Element element;
		
		switch(type){	
			case CHANGE_VALUE:
				throw new IllegalFileModification("Cannot modify that value");
				
			case CHANGE_NAME:
				element = (Element) fileModel.getNodeById(command.getItemId());					
				fileModel.getDocument().renameNode(element, null, command.getNewValue());	
				
				if(!nodeValidation.isValidNode(element, command.getNewValue()))
					throw new IllegalFileModification("Invalid name for element");
				break;
				
			default:
				break;				
		}
	}

	@Override
	public void addElement(Node parent) throws IllegalFileModification {		
		Element newElement = fileModel.getDocument().createElement("NEW");
		parent.appendChild(newElement);
		fileModel.addItem(newElement);	
		if(!nodeValidation.isChildAllowed(parent))
			throw new IllegalFileModification("Children are not allowed here");	
	}

	@Override
	public void addAttribute(Node parent) throws IllegalFileModification {				
		Element element = (Element) parent;
		Attr attribute = fileModel.getDocument().createAttribute("NEW");
		element.setAttributeNode(attribute);
		fileModel.addItem(attribute);
		if(!nodeValidation.isAttributeAllowed(parent))
			throw new IllegalFileModification("Attributes are not allowed here");	
	}

	@Override
	public void remove(Command command) {
		Element element = (Element) fileModel.getNodeById(command.getItemId());
		element.getParentNode().removeChild(element);	
	}
}
