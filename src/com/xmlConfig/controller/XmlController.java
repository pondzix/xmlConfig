package com.xmlConfig.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.xmlConfig.domain.Command;
import com.xmlConfig.exception.IllegalFileModification;
import com.xmlConfig.exception.InvalidInputParameterException;
import com.xmlConfig.exception.SolutionNotFoundException;
import com.xmlConfig.service.FileService;
import com.xmlConfig.service.UnitService;
import com.xmlConfig.view.XmlView;

public class XmlController {
	
	private XmlView view;
	private FileService fileService = new FileService();
	private UnitService unitService = new UnitService();
	
	public XmlController(XmlView view) {
		this.view = view;
	}

	public void getFile(String path) {
		try {
			Document doc = fileService.getFile(path);
			unitService.setFileService(fileService);
			view.displayFile(doc);
		} catch (IOException | SAXException | ParserConfigurationException | InvalidInputParameterException e) {
			e.printStackTrace();
			view.showLoadingFileFail();
		}		
	}

	public List<String> getFileList() {
		return fileService.getFileList();	
	}

	public void saveFile() {
		try {
			fileService.saveFile();
			view.showSaveSucces();
		} catch (ParserConfigurationException | TransformerException e) {	
			view.showSaveFail();		
		}	 
	}

	public void updateNameOrValue(Command command) {
		String gauge;
		try {
			fileService.updateNameOrValue(command);
			gauge = unitService.calculateGauge(command.getNewValue());
			view.updateGauge(command.getItemId(), gauge);
		} catch (IllegalFileModification | SolutionNotFoundException | InvalidInputParameterException e) {
			view.showMessage(e.getMessage());
		}
	}

	public void addElement(Command command) {
		try {
			fileService.addChildElementToSelectedItem(command);
			view.createNewElement();
		} catch (IllegalFileModification e) {
			view.showMessage(e.getMessage());
		}
		
	}

	public void addAttribute(Command command) {
		try {
			fileService.addAttributeToSelectedItem(command);
			view.createNewAttribute();
		} catch (IllegalFileModification e) {
			view.showMessage(e.getMessage());
		}
	}

	public void removeItem(Command command) {
		fileService.removeItem(command);
		view.removeItem();		
	}
	
	
	public String calculateGauge(String param){
		String gauge = "";
		try {
			gauge = unitService.calculateGauge(param);
		} catch (SolutionNotFoundException e) {
			
		} catch (InvalidInputParameterException e) {
			
		}		
		return gauge;
	}

	public Map<Integer, String> getGauges() {
		
		try {
			return unitService.getGauges();
		} catch (SolutionNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidInputParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	

}
