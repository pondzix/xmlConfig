package com.xmlConfig.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.xmlConfig.dao.FileDao;
import com.xmlConfig.dao.FileDaoImpl;
import com.xmlConfig.domain.Command;
import com.xmlConfig.domain.XmlFileAdapter;
import com.xmlConfig.exception.IllegalFileModification;

public class FileService {

	private XmlFileAdapter fileModel;	
	private FileDao fileDao = new FileDaoImpl();
	
	
    public XmlFileAdapter getFileModel() {
		return fileModel;
	}
  
	public Document getFile(String fileName) throws IOException, SAXException, ParserConfigurationException {
		if(!fileName.isEmpty()){
			Document doc =  fileDao.getXmlFile(fileName);
			fileModel = new XmlFileAdapter(doc, fileName);
			return doc;
		}
		return null;
	}
	
	public List<String> getFileList() {	
		return fileDao.getFileList();
	}
	
	public void saveFile() throws ParserConfigurationException, TransformerException {
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
