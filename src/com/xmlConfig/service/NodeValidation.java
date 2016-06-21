package com.xmlConfig.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;
import org.yaml.snakeyaml.Yaml;

@SuppressWarnings("rawtypes")
public class NodeValidation {
	
	private Map map;
	
	public NodeValidation() {
		init();
	}

	private void init() {
		InputStream input = null;
		try {
			input = new FileInputStream(new File("C:/xmlTest/elements.yml"));
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		}
		Yaml yaml = new Yaml();	
		map = (Map)yaml.load(input);	
	}

	public boolean isValidNode(Node node, String name){
		if(isValidName(name)){
			Node parent = node.getParentNode();
			Map parentProperties = getElementProperties(parent.getNodeName());
			Map childProperties = getElementProperties(name);
			if(parentProperties.containsKey("children"))
				return checkChildren(parentProperties, childProperties);							
			else {
				Map mainTypes = (Map) map.get("types");
				Map type = (Map) mainTypes.get(parentProperties.get("type"));
				if(type.containsKey("children"))
					return checkChildren(type, childProperties);
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private boolean checkChildren(Map map, Map childProperties){
		List<Map> types = (List<Map>) map.get("children");
		for(Map m: types)					
			if(m.get("type").equals(childProperties.get("type")))
				return true;	
		
		return false;
	}
	
	private Map getElementProperties(String nodeName) {	
		return (Map) map.get(nodeName);
	}

	private boolean isValidName(String name){
		return map.containsKey(name);
	}

}
