package com.xmlConfig.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlFileAdapter {
	
	private Document document;
	private String fileName;
	private Map<Integer, Node> elementMap = new HashMap<>();
	private int nodeCounter;

	public XmlFileAdapter(Document document, String fileName) {
		this.document = document;
		this.fileName = fileName;
		buildElementMap(document.getDocumentElement());
	}

	public Document getDocument() {
		return document;
	}

	public void setDoc(Document document) {
		this.document = document;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public Node getNodeById(int id){
		return elementMap.get(id);		
	}
	
	public void removeItem(int id){

	}
	
	public void addItem(Node element){
		elementMap.put(nodeCounter++, element);	
	}
	
	public void updateItem(int itemId, Node element){
		elementMap.put(itemId, element);
	}
	
	private void buildElementMap(Node element){
		
		elementMap.put(nodeCounter++, element);
		addAttributesToList(element);
		NodeList childrenList = element.getChildNodes();
		for(int i = 0; i < childrenList.getLength(); i++)
			if(childrenList.item(i) instanceof Element)
				buildElementMap(childrenList.item(i));
		
	}

	private void addAttributesToList(Node element) {
		if(element.hasAttributes()){
			NamedNodeMap attr = element.getAttributes();
			for(int i = 0; i < attr.getLength(); i++)
				if(attr.item(i) instanceof Attr)
				elementMap.put(nodeCounter++, (Attr)attr.item(i));
			
		}
	}
}
