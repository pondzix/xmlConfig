package com.xmlConfig.dao;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.xmlConfig.domain.XmlFileAdapter;



public interface FileDao {
	
	public Document getXmlFile(String path) throws IOException, SAXException, ParserConfigurationException;

	public List<String> getFileList();

	public void saveFile(XmlFileAdapter fileModel) throws ParserConfigurationException, TransformerConfigurationException, TransformerException;

}
