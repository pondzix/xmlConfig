package com.xmlConfig.service;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.xmlConfig.dao.FileDao;
import com.xmlConfig.dao.FileDaoImpl;
import com.xmlConfig.domain.XmlFileAdapter;

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
	
	public void updateFile(String propertyName, int itemId, int parentItemId, String newValue) {
		Element element;
		Attr tempNode;

		if(fileModel.getNodeById(itemId) instanceof Attr){
			tempNode = (Attr) fileModel.getNodeById(itemId);
			element = removeAttribute(tempNode, parentItemId);
			fileModel.removeItem(itemId);
			if(propertyName.equals("Value")){
				element.setAttribute(tempNode.getName(), newValue);	
				fileModel.addItem(itemId, element.getAttributeNode(tempNode.getName()));
			}
			else{
				element.setAttribute(newValue, tempNode.getValue());
				fileModel.addItem(itemId, element.getAttributeNode(newValue));
			}	
		}else{
			element = (Element) fileModel.getNodeById(itemId);
			fileModel.getDoc().renameNode(element, null, newValue);	
		}
	}
	
	private Element removeAttribute(Attr attribute,  int parentItemId){
		Element element = (Element) fileModel.getNodeById(parentItemId);
		element.removeAttributeNode(attribute);
		return element;
	}

}
