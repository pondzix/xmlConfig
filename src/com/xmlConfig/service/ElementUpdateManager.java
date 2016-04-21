package com.xmlConfig.service;


import org.w3c.dom.Element;

import com.xmlConfig.domain.ItemDTO;
import com.xmlConfig.domain.XmlFileAdapter;
import com.xmlConfig.exception.IllegalFileModification;

public class ElementUpdateManager implements UpdateManager{

	private XmlFileAdapter fileModel;
	
	public ElementUpdateManager(XmlFileAdapter fileModel) {
		this.fileModel = fileModel;
	}
	
	@Override
	public void update(ItemDTO item) throws IllegalFileModification {
		if(item.getPropertyName().equals("Value"))
			throw new IllegalFileModification();
		else{
			Element element = (Element) fileModel.getNodeById(item.getItemId());
			fileModel.getDocument().renameNode(element, null, item.getNewValue());	
		}
		
	}

}
