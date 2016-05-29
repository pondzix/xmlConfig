package com.xmlConfig.domain;

import java.util.ArrayList;
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
	private List<Integer> valuesWithGauge = new ArrayList<>();
	private Map<Integer, Integer> equations = new HashMap<>();
	private int nodeCounter;
	private boolean hasUnits;

	public XmlFileAdapter(Document document, String fileName) {
		this.document = document;
		this.fileName = fileName;
		buildElementMap(document.getDocumentElement());
		checkIfIsUnitsElement(document);
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
	
	public boolean hasUnits(){
		return hasUnits;
	}
	
	public Map<String, String> getEquations(){
		Map<String, String> eq = new HashMap<>();
		for(Map.Entry<Integer, Integer> entry: equations.entrySet())
			eq.put(elementMap.get(entry.getKey()).getNodeValue(), elementMap.get(entry.getValue()).getNodeValue());
		
		return eq;
	}
	
	private void buildElementMap(Node element){	
		elementMap.put(nodeCounter++, element);		
		if(isEquationParameter(element))
			equations.put(nodeCounter + 1, nodeCounter);
			
		addAttributesToList(element);
		NodeList childrenList = element.getChildNodes();
		for(int i = 0; i < childrenList.getLength(); i++)
			if(childrenList.item(i) instanceof Element){
				buildElementMap(childrenList.item(i));
			}
		
	}

	private void addAttributesToList(Node element) {
		if(element.hasAttributes()){
			NamedNodeMap attrs = element.getAttributes();
			for(int i = 0; i < attrs.getLength(); i++)
				if(attrs.item(i) instanceof Attr){
					Attr attr = (Attr)attrs.item(i);							
					elementMap.put(nodeCounter, attr);
					valuesWithGauge.add(nodeCounter++);
				}
			
		}
	}
	
	private boolean isEquationParameter(Node item) {
		Node parent = item.getParentNode();
		return item.getNodeName().equals("Params") && parent.getNodeName().equals("Units");
	}

	private void checkIfIsUnitsElement(Document doc){
		hasUnits = doc.getElementsByTagName("Units").getLength() > 0;
	}
	
	
}
