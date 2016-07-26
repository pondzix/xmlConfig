package com.xmlConfig.view.jsComponent;

import com.vaadin.annotations.JavaScript;
import com.xmlConfig.view.JsXmlComponent;

@JavaScript({"js_box_component.js"})
public class JsBoxComponent extends JsXmlComponent {
	private static final long serialVersionUID = 1L;

	@Override
	protected JsBoxState getState(){
		return (JsBoxState) super.getState();
	}

	@Override
	public void setState(int id, String name, String value) {
		getState().boxId = id;
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
