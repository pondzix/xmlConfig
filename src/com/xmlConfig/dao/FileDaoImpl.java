package com.xmlConfig.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;





import org.xml.sax.SAXException;

import com.xmlConfig.domain.XmlFileAdapter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class FileDaoImpl implements FileDao{

	
	private String directoryPath = "";

	@Override 
	public Document getXmlFile(String fileName) throws  IOException, SAXException, ParserConfigurationException {
		File fXmlFile = new File(directoryPath + fileName);
		return  createDocumentBuilder().parse(fXmlFile);
	}

	@Override
	public List<String> getFileList() {
		
		List<String> fileNameList = new ArrayList<>();
		File directory = new File(directoryPath);
		File[] fileList = directory.listFiles();
		for(File f: fileList)
			fileNameList.add(f.getName());
		
		return fileNameList;
	}

	@Override
	public void saveFile(XmlFileAdapter fileModel) throws ParserConfigurationException, TransformerException {
        Document doc = fileModel.getDoc();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(directoryPath + fileModel.getFileName()));
		transformer.transform(source, result);
		
	}
	
	private DocumentBuilder createDocumentBuilder() throws ParserConfigurationException{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        return dbFactory.newDocumentBuilder(); 
		
	}

}
