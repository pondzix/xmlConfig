package com.xmlConfig.view;

import org.w3c.dom.Document;

public interface XmlView {
	
	public void displayFile(Document doc);

	public void showSaveSucces();

	public void showSaveFail();

	public void showLoadingFileFail();

	public void showModificationFail();

}
