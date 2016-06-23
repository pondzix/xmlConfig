package com.xmlConfig.view;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;

@JavaScript({"com_xmlConfig_view_JsLabel.js"})
public class JsLabel extends AbstractJavaScriptComponent{
	
	private static final long serialVersionUID = 1L;
	
	public JsLabel(String value){
		getState().nx = value;
		getState().ny = value;
		getState().nz = value;
	}
	public void setState(String nx, String ny, String nz){
		getState().nx = nx;
		getState().ny = ny;
		getState().nz = nz;
	}
	protected JsLabelState getState(){
		return (JsLabelState) super.getState();
	}

}
