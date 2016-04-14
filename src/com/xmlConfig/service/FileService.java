package com.xmlConfig.service;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.xmlConfig.dao.FileDao;
import com.xmlConfig.dao.FileDaoImpl;
import com.xmlConfig.domain.XmlFileAdapter;

public class FileService {

	XmlFileAdapter file;
	
	private FileDao fileDao = new FileDaoImpl();
	public Document getFile(String path) throws IOException, SAXException, ParserConfigurationException {
		Document doc =  fileDao.getXmlFile(path);
		file = new XmlFileAdapter(doc.getDocumentElement());
		return doc;
	}

}
