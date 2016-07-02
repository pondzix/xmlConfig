package com.xmlConfig.view;

import java.util.ArrayList;
import java.util.List;


import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;

@JavaScript({"com_xmlConfig_view_JsLabel.js"})
public class JsComp extends AbstractJavaScriptComponent{
	
	private static final long serialVersionUID = 1L;
	private List<String> dimensions = new ArrayList<>();
	
	public JsComp(){
		getState().box = new String[6];
		getState().geometry = new String[3];
		init(getState().box);
		init(getState().geometry);
		//init(getState().wedge);
		initMap();
	}
	
	public void setGeometryState(String nx, String ny, String nz){
		getState().geometrySizeChange = false;
		getState().boxSizeChange = false;
		getState().geometry[0] = nx;
		getState().geometry[1] = ny;
		getState().geometry[2] = nz;
	}
	
	public void setBoxState(String nx, String ny, String nz,
			                String dx, String dy, String dz){	
		getState().box[0] = nx;
		getState().box[1] = ny;
		getState().box[2] = nz;
		getState().box[3] = dx;
		getState().box[4] = dy;
		getState().box[5] = dz;		
	}
	
	public void setWedgeState(String nx, String ny, String nz,
            String dx, String dy, String dz){	
		getState().wedge[0] = nx;
		getState().wedge[1] = ny;
		getState().wedge[2] = nz;
		getState().wedge[3] = dx;
		getState().wedge[4] = dy;
		getState().wedge[5] = dz;		
	}
	
	public void updateGeometryState(String name, String value){
		getState().boxSizeChange = false;
		getState().geometrySizeChange = true;
		getState().wedgeSizeChange = false;
		getState().geometry[dimensions.indexOf(name)] = value;
	}
	
	public void updateBoxState(String name, String value){
		getState().geometrySizeChange = false;
		getState().boxSizeChange = true;
		getState().wedgeSizeChange = false;
		getState().box[dimensions.indexOf(name)] = value;
	}
	
	public void updateWedgeState(String name, String value){
		getState().geometrySizeChange = false;
		getState().boxSizeChange = false;
		getState().wedgeSizeChange = true;
		getState().wedge[dimensions.indexOf(name)] = value;
	}
	
	private void initMap(){
		dimensions.add("nx");
		dimensions.add("ny");
		dimensions.add("nz");
		dimensions.add("dx");
		dimensions.add("dy");
		dimensions.add("dz");
	}
			
	private void init(String[] tab){	
		for(int i = 0; i < tab.length; i++)
			tab[i] = "0";		
	}
	
	protected JsLabelState getState(){
		return (JsLabelState) super.getState();
	}

}
