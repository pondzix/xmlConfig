package com.xmlConfig.dao;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;





import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class FileDaoImpl implements FileDao{

	@Override 
	public Document getXmlFile(String path) throws  IOException, SAXException, ParserConfigurationException {
		File fXmlFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); 
		return  dBuilder.parse(fXmlFile);
	}

}
