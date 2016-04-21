package com.xmlConfig.service;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import com.xmlConfig.domain.ItemDTO;
import com.xmlConfig.domain.XmlFileAdapter;

public class AttributeUpdateManager implements UpdateManager{

	private XmlFileAdapter fileModel;
	
	public AttributeUpdateManager(XmlFileAdapter fileModel) {
		this.fileModel = fileModel;
	}
	
	@Override
	public void update(ItemDTO item) {
		Attr tempNode = (Attr) fileModel.getNodeById(item.getItemId());
		Element element = removeAttribute(tempNode, item.getParentItemId());
		fileModel.removeItem(item.getItemId());
		
		if(item.getPropertyName().equals("Value")){
			element.setAttribute(tempNode.getName(), item.getNewValue());	
			fileModel.addItem(item.getItemId(), element.getAttributeNode(tempNode.getName()));
		}
		else{
			element.setAttribute(item.getNewValue(), tempNode.getValue());
			fileModel.addItem(item.getItemId(), element.getAttributeNode(item.getNewValue()));
		}	
	}
	
	private Element removeAttribute(Attr attribute,  int parentItemId){
		Element element = (Element) fileModel.getNodeById(parentItemId);
		element.removeAttributeNode(attribute);
		return element;
	}

}
