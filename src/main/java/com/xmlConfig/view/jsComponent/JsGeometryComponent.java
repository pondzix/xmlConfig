package com.xmlConfig.view.jsComponent;


import com.vaadin.annotations.JavaScript;
import com.xmlConfig.view.JsXmlComponent;


@JavaScript({"js_geometry_component.js"})
public class JsGeometryComponent  extends JsXmlComponent {
	private static final long serialVersionUID = 1L;

	@Override
	protected JsGeometryState getState(){
		return (JsGeometryState) super.getState();
	}

	@Override
	public void setState(int id, String name, String value) {
		getState().sizeChange = false;
		setProperty(name, value);
	}

	@Override
	public void setProperty(String name, String value){
		switch(name){
		case "nx":
			getState().nx = value;
			break;
		case "ny":
			getState().ny = value;
			break;
		case "nz":
			getState().nz = value;
			break;		
		}
	}

}
