package com.xmlConfig.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.xmlConfig.dao.FileDao;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.xmlConfig.dao.XmlFileDaoImpl;
import com.xmlConfig.domain.Command;
import com.xmlConfig.domain.FileAdapter;
import com.xmlConfig.exception.IllegalFileModification;

public class FileService {

	private FileAdapter fileModel;
	private FileDao fileDao = new XmlFileDaoImpl();
	
	
    public FileAdapter getFileModel() {
		return fileModel;
	}
  
	public Document getFile(String fileName) throws IOException, SAXException, ParserConfigurationException {
		if(!fileName.isEmpty()){
			File file =  fileDao.getFile(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			Document doc = dbFactory.newDocumentBuilder().parse(file);
			fileModel = new FileAdapter(doc, fileName);
			return doc;
		}
		return null;
	}
	
	public List<String> getFileList() {	
		return fileDao.getFileList();
	}
	
	public void saveFile() throws ParserConfigurationException, TransformerException, IllegalFileModification {
			fileDao.saveFile(fileModel);	
	}
	
	public void addChildElementToSelectedItem(Command command) throws IllegalFileModification{
		Node node = fileModel.getNodeById(command.getItemId());
		getUpdateServiceByNodeType(node).addElement(node);		
	}
	
	public void addAttributeToSelectedItem(Command command) throws IllegalFileModification{
		Node node = fileModel.getNodeById(command.getItemId());
		getUpdateServiceByNodeType(node).addAttribute(node);
	}
	
	public void updateNameOrValue(Command command) throws IllegalFileModification{
		Node node = fileModel.getNodeById(command.getItemId());
		getUpdateServiceByNodeType(node).update(command);
		
	}
	
	public void removeItem(Command command) {
		Node node = fileModel.getNodeById(command.getItemId());
		getUpdateServiceByNodeType(node).remove(command);		
	}
		
	private  UpdateService getUpdateServiceByNodeType(Node node){
		if(fileModel.isEquationParameter(node))
			return new UnitsUpdateService(fileModel);
		
		if(node instanceof Attr)
			return new AttributeUpdateService(fileModel);
		else
			return new ElementUpdateService(fileModel);			
	}

	public Map<Integer, String> getValuesWithGauge() {	
		return fileModel.getValuesWithGauge();
	}

}
