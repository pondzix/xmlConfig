package com.xmlConfig.domain;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class XmlFileAdapter {
	
	private Element rootElement;
	private Document doc;
	private String fileName;
	private List<Node> elementList = new ArrayList<>();

	public XmlFileAdapter(Document doc, String fileName) {
		this.setDoc(doc);
		this.fileName = fileName;
		rootElement = doc.getDocumentElement();
		buildElementList(rootElement);
	}

	public Element getRootElement() {
		return rootElement;
	}

	public void setRootElement(Element rootElement) {
		this.rootElement = rootElement;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public Node getNodeById(int id){
		System.out.println(elementList.get(id));
		return elementList.get(id);		
	}
	
	public void removeItem(int id){
		elementList.remove(id);
	}
	
	private void buildElementList(Element element){
		
		elementList.add(element);
		addAttributesToList(element);
		NodeList childrenList = element.getChildNodes();
		for(int i = 0; i < childrenList.getLength(); i++)
			if(!(childrenList.item(i) instanceof Text))
				buildElementList((Element) childrenList.item(i));
		
	}

	private void addAttributesToList(Node element) {
		if(element.hasAttributes()){
			NamedNodeMap attr = element.getAttributes();
			for(int i = 0; i < attr.getLength(); i++)
				if(!(attr instanceof Text))
				elementList.add((Attr)attr.item(i));
			
		}
	}

	public void addItem(int itemId, Attr attr) {
	elementList.add(itemId, attr);
		
	}
	
	
	
	
	

}
