package com.xmlConfig.view;

import org.w3c.dom.Document;

public interface XmlView {
	
	public void displayFile(Document doc);
	
	public void showMessage(String message);
	
	public void createNewElement();
	
	public void createNewAttribute();
	
	public void removeItem();
	
	public void updateGauge(int id, String gauge);

}
