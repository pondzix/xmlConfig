package com.xmlConfig.view;

public enum JsXmlComponentType {
	GEOMETRY("Geometry"),
	BOX("Box"),
	WEDGE("Wedge");
	
	private String name;
	
	JsXmlComponentType(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
}
