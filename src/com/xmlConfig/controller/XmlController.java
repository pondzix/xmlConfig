package com.xmlConfig.controller;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.xmlConfig.service.FileService;
import com.xmlConfig.view.XmlUi;

public class XmlController {
	
	private XmlUi view;
	private FileService service = new FileService();
	
	public XmlController(XmlUi view) {
		this.view = view;
	}

	public void getFile(String path) {
		
		try {
			view.buildTree(service.getFile(path));
		} catch (IOException | SAXException | ParserConfigurationException e) {
			
			e.printStackTrace();
		}
		
	}
	
	
	

}
