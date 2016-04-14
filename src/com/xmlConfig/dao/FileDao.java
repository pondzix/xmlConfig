package com.xmlConfig.dao;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;



public interface FileDao {
	
	public Document getXmlFile(String path) throws IOException, SAXException, ParserConfigurationException;

}
