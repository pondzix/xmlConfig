package com.xmlConfig.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;


import com.xmlConfig.domain.FileAdapter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XmlFileDaoImpl extends FileDao {

	@Override
	public void saveFile(FileAdapter fileModel) throws ParserConfigurationException, TransformerException {
        Document doc = fileModel.getDocument();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(getDirectoryPath() + fileModel.getFileName() + getExtension()));
		transformer.transform(source, result);
	}

	@Override
	public String getExtension() {
		return ".xml";
	}
}
