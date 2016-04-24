package com.xmlConfig.controller;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.xmlConfig.domain.Command;
import com.xmlConfig.exception.IllegalFileModification;
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
			view.displayFile(service.getFile(path));
		} catch (IOException | SAXException | ParserConfigurationException e) {
			view.showLoadingFileFail();
		}		
	}

	public List<String> getFileList() {
		return service.getFileList();	
	}

	public void saveFile() {
		try {
			service.saveFile();
			view.showSaveSucces();
		} catch (ParserConfigurationException | TransformerException e) {	
			view.showSaveFail();		
		}	 
	}

	public void updateNameOrValue(Command command) {
		try {
			service.updateNameOrValue(command);
		} catch (IllegalFileModification e) {
			view.showModificationFail();
		}
	}

	public void addElement(Command command) {
		try {
			service.addChildElementToSelectedItem(command);
			view.createNewElement();
		} catch (IllegalFileModification e) {
			view.showModificationFail();
		}
		
	}

	public void addAttribute(Command command) {
		try {
			service.addAttributeToSelectedItem(command);
			view.createNewAttribute();
		} catch (IllegalFileModification e) {
			view.showModificationFail();
		}
	}

	public void removeItem(int selectedItemId) {
		service.removeItem(selectedItemId);
		view.removeItem();		
	}
	
	
	

}
