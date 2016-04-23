package com.xmlConfig.service;

import java.io.IOException;
import java.util.List;

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
	
	public void addChildElementToSelectedItem(int selectedItemId){
		//Node node = fileModel.getNodeById(item.getItemId());
		
		
	}
	
	public void addAttributeToSelectedItem(int selectedItemId){
		
		
		
	}
	
	
	public void removeItem(){
		
		
		
	}
		
	public void updateFileItem(Command item) throws IllegalFileModification{
		Node node = fileModel.getNodeById(item.getItemId());
		
		if(node instanceof Attr)
			update(new AttributeUpdateManager(fileModel), item);
		else
			update(new ElementUpdateManager(fileModel), item);			
	}
	
	private void update(UpdateManager manager, Command item) throws IllegalFileModification{
		manager.update(item);		
	}

}
