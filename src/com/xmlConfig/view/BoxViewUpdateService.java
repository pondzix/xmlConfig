package com.xmlConfig.view;

import org.w3c.dom.Element;

public class BoxViewUpdateService {

	private JsLabel connector;
	
	public BoxViewUpdateService(JsLabel connector){
		this.connector = connector;		
	}
		
	public void setGeometry(Element geometry){
		String nx = convertDimension("nx", geometry);
		String ny = convertDimension("ny", geometry);
		String nz = convertDimension("nz", geometry);
		connector.setGeometryState(nx, ny, nz);
	}
	
	public void setBox(Element box){
		connector.setBoxState(convertDimension("nx", box),
							  convertDimension("ny", box),
							  convertDimension("nz", box),
							  convertDimension("dx", box),
							  convertDimension("dy", box),
							  convertDimension("dz", box));
	}
	
	public void setWedge(Element wedge){
		connector.setWedgeState(convertDimension("nx", wedge),
				  convertDimension("ny", wedge),
				  convertDimension("nz", wedge),
				  convertDimension("dx", wedge),
				  convertDimension("dy", wedge),
				  convertDimension("dz", wedge));
		
	}
	
	public void updateGeometry(String name, String value){	
		connector.updateGeometryState(name, convertDimension(value));
	}
	
	public void updateBox(String name, String value){
		connector.updateBoxState(name, convertDimension(value));
	}
	
	public void updateWedge(String name, String value){
		connector.updateWedgeState(name, convertDimension(value));
	}
	
	private String convertDimension(String name, Element element){
		String value = element.getAttribute(name);
		return convertDimension(value);		
	}
	
	private String convertDimension(String value){
		if(value.contains("m"))
			return value.replace("m", "") + "00";
		
		return value;
	}
}
