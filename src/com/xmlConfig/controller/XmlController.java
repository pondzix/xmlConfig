package com.xmlConfig.controller;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.xmlConfig.domain.ItemDTO;
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

	public void updateFile(ItemDTO item) {
		try {
			service.updateFile(item);
		} catch (IllegalFileModification e) {
			view.showModificationFail();
		}
	}
	
	
	

}
