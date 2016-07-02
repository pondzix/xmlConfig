package com.xmlConfig.view.jsComponent;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.xmlConfig.view.JsXmlComponent;

@JavaScript({"com_xmlConfig_view_jsComponent_JsWedgeComponent.js"})
public class JsWedgeComponent extends AbstractJavaScriptComponent implements JsXmlComponent{
	private static final long serialVersionUID = 1L;

	@Override
	public void updateState(String name, String value) {
		getState().sizeChange = true;
		setProperty(name, value);
	}
	
	@Override
	protected JsWedgeState getState(){
		return (JsWedgeState) super.getState();
	}

	@Override
	public void setState(int id, String name, String value) {
		getState().wedgeId = id;
		getState().sizeChange = false;
		setProperty(name, value);
		
	}
	
	private void setProperty(String name, String value){
		switch(name){
			case "direction":
				getState().orientation = value;
				break;
			case "nx":
				getState().nx = value;
				break;
			case "ny":
				getState().ny = value;
				break;
			case "nz":
				getState().nz = value;
				break;	
			case "dx":
				getState().dx = value;
				break;
			case "dy":
				getState().dy = value;
				break;
			case "dz":
				getState().dz = value;
				break;	
		}			
	}
}
