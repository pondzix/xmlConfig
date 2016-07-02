package com.xmlConfig.view.jsComponent;


import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.xmlConfig.view.JsXmlComponent;

@JavaScript({"com_xmlConfig_view_jsComponent_JsGeometryComponent.js"})
public class JsGeometryComponent  extends AbstractJavaScriptComponent implements JsXmlComponent{
	private static final long serialVersionUID = 1L;

	@Override
	public void updateState(String name, String value) {
		getState().sizeChange = true;
		setProperty(name, value);
	}
	
	@Override
	protected JsGeometryState getState(){
		return (JsGeometryState) super.getState();
	}

	@Override
	public void setState(int id, String name, String value) {
		getState().sizeChange = false;
		setProperty(name, value);
	}
	
	private void setProperty(String name, String value){
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
