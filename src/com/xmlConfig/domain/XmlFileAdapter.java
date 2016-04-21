package com.xmlConfig.domain;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlFileAdapter {
	
	private Document document;
	private String fileName;
	private List<Node> elementList = new ArrayList<>();

	public XmlFileAdapter(Document document, String fileName) {
		this.document = document;
		this.fileName = fileName;
		buildElementList(document.getDocumentElement());
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
		return elementList.get(id);		
	}
	
	public void removeItem(int id){
		elementList.remove(id);
	}
	
	private void buildElementList(Node element){
		
		elementList.add(element);
		addAttributesToList(element);
		NodeList childrenList = element.getChildNodes();
		for(int i = 0; i < childrenList.getLength(); i++)
			if(childrenList.item(i) instanceof Element)
				buildElementList(childrenList.item(i));
		
	}

	private void addAttributesToList(Node element) {
		if(element.hasAttributes()){
			NamedNodeMap attr = element.getAttributes();
			for(int i = 0; i < attr.getLength(); i++)
				if(attr.item(i) instanceof Attr)
				elementList.add((Attr)attr.item(i));
			
		}
	}

	public void addItem(int itemId, Attr attr) {
		elementList.add(itemId, attr);	
	}
}
