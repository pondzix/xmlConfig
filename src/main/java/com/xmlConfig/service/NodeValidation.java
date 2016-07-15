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
	private Map mainTypes;
	private final String CHILDREN = "children";
	private final String TYPES = "types";
	private final String TYPE = "type";
	private final String ATTRIBUTE = "attr";
	private final String NAME= "name";
	
	public NodeValidation() {
		init();
	}

	private void init() {
		InputStream input = null;
		try {
			input = new FileInputStream(new File("/var/lib/openshift/57123bcb89f5cff1ef0000cc/app-root/data/elements.yml"));
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		}
		Yaml yaml = new Yaml();	
		map = (Map)yaml.load(input);	
		mainTypes = (Map) map.get(TYPES);
	}

	public boolean isValidNode(Node node, String name){
		if(isValidName(name)){
			Node parent = node.getParentNode();
			Map parentProperties = getElementProperties(parent.getNodeName());
			Map type = (Map) mainTypes.get(parentProperties.get(TYPE));
			Map childProperties = getElementProperties(name);
		    return checkAllowedChildrenNames(parentProperties, childProperties) ||
				   checkAllowedChildrenNames(type, childProperties);							
		}
		return false;
	}
	
	public boolean isValidAttribute(Node owner, String name){
		System.out.println(owner);
		Map ownerProperties = getElementProperties(owner.getNodeName());
		Map type = (Map) mainTypes.get(ownerProperties.get(TYPE));
		return checkAllowedAttributes(ownerProperties, name) || 
			   checkAllowedAttributes(type, name);
		
	}

	public boolean isChildAllowed(Node node){
		return isNewNodeAllowed(node, CHILDREN);
	}
	
	public boolean isAttributeAllowed(Node node){
		return isNewNodeAllowed(node, ATTRIBUTE);
	}
	
	private boolean isNewNodeAllowed(Node node, String property){
		Map nodeProperties = getElementProperties(node.getNodeName());
		if(nodeProperties != null){
			Object type = nodeProperties.get(TYPE);
			return nodeProperties.containsKey(property) ||
				   ((Map) mainTypes.get(type)).containsKey(property);
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
    private boolean checkAllowedAttributes(Map map, String name) {
    	if(map.containsKey(ATTRIBUTE)){			
			List<Map> types = (List<Map>) map.get(ATTRIBUTE);
			for(Map m: types)					
				if(m.get(NAME).equals(name))
					return true;	
		}	
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private boolean checkAllowedChildrenNames(Map map, Map childProperties){
		if(map.containsKey(CHILDREN)){
			List<Map> types = (List<Map>) map.get(CHILDREN);
			for(Map m: types)					
				if(m.get(TYPE).equals(childProperties.get(TYPE)))
					return true;	
		}	
		return false;
	}
	
	private Map getElementProperties(String nodeName) {	
		return (Map) map.get(nodeName);
	}

	private boolean isValidName(String name){
		return map.containsKey(name);
	}

}
