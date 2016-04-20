package com.xmlConfig.controller;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.xmlConfig.service.FileService;
import com.xmlConfig.view.XmlView;

public class XmlController {
	
	private XmlView view;
	private FileService service = new FileService();
	
	public XmlController(XmlView view) {
		this.view = view;
	}

	public void getFile(String path) {
		try {
			System.out.println("sdsd");
			Document doc;
			if((doc = service.getFile(path)) != null)
				view.displayFile(doc);
		} catch (IOException | SAXException | ParserConfigurationException e) {
			e.printStackTrace();
		}		
	}

	public List<String> getFileList() {
		return service.getFileList();	
	}

	public void saveFile() {
		try {
			service.saveFile();
			view.showSaveSucces();
		} catch (TransformerConfigurationException
				| ParserConfigurationException e) {	
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}	 
	}

	public void updateFile(String porpertyName, int itemId, int parentItemId, String newValue) {
		service.updateFile(porpertyName, itemId, parentItemId, newValue);
	}
	
	
	

}
