package com.xmlConfig.view;

import com.vaadin.ui.AbstractJavaScriptComponent;
import com.xmlConfig.view.jsComponent.JsXmlComponentState;

public abstract class JsXmlComponent extends AbstractJavaScriptComponent {

    public abstract void setProperty(String name, String value);

    public abstract void setState(int id, String name, String value);

    @Override
    protected  JsXmlComponentState getState(){
        return (JsXmlComponentState) super.getState();
    }

    public void updateState(String name, String value) {
        getState().sizeChange = true;
        setProperty(name, value);
    }


}
