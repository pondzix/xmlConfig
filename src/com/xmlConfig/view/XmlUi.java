package com.xmlConfig.view;



import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Text;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.xmlConfig.controller.XmlController;

@SuppressWarnings("serial")
@Theme("firstvaad")
public class XmlUi extends UI { 


	private XmlController controller = new XmlController(this);
	private TreeTable tree;
	private int nodeCounter = 0;
	
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = XmlUi.class)
	public static class Servlet extends VaadinServlet {
	}
 
	@Override
	protected void init(VaadinRequest request) {

		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);  
		initTree();
	    controller.getFile("C:/xmlTest/ahmed.xml");
		layout.addComponent(tree);
		
	}
	private void initTree(){
		tree = new TreeTable("XML Configuration");
		tree.addContainerProperty("Property Name", String.class, null);
		tree.addContainerProperty("Value", TextField.class, null);
		tree.setWidth("400");
		
	}
	public void buildTree(Document doc){
		    Element root = doc.getDocumentElement();
		    Object rootItem = root.getNodeName();
	        int parentId = nodeCounter;
	        
	        tree.addItem(new Object[]{rootItem, null}, parentId);
	        addAttributesToTree(root, parentId);	   
	        addChildrenToTree(root.getChildNodes(), rootItem, parentId);
		
	}
	
	private void addChildrenToTree(NodeList children, Object parent, int id) {
	    if (children.getLength() > 0) {
	    	
	    	int parentId = id;
	        for (int i = 0; i < children.getLength(); i++) {
	        	int childId = ++nodeCounter;
	            Node node = children.item(i);
	            
	            if(node instanceof Text)
	            	continue;
	            Object child = node.getNodeName();
	            tree.addItem(new Object[]{child, null}, childId);
	            tree.setParent(childId, parentId);
	            
	            addAttributesToTree(node, childId);
	            addChildrenToTree(node.getChildNodes(), child, childId);
	        }
	    }
	}
	
	
	private <T extends Node> void addAttributesToTree(T node, int parentId){
		 if(node.hasAttributes()){
	        	NamedNodeMap attr = node.getAttributes();
	        	for(int j = 0; j < attr.getLength(); j++){
	        		int childId = ++nodeCounter;
	        		Attr attribute = (Attr) attr.item(j);	
	        		TextField details = new TextField();
	        		details.setValue(attribute.getValue());
	        		tree.addItem(new Object[]{attribute.getName(), details}, childId);
	        		tree.setParent(childId, parentId);
	        		tree.setChildrenAllowed(childId, false);
	        	}
	        }
	}
	


}