package com.xmlConfig.view;



import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList; 
import org.w3c.dom.NamedNodeMap;

import javax.servlet.annotation.WebServlet;

import com.vaadin.addon.contextmenu.ContextMenu;
import com.vaadin.addon.contextmenu.Menu;
import com.vaadin.addon.contextmenu.MenuItem;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ContextClickEvent.ContextClickNotifier;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.xmlConfig.controller.XmlController;
import com.xmlConfig.domain.ActionType;
import com.xmlConfig.domain.Command;

@SuppressWarnings("serial")
@Theme("firstvaad")
public class XmlMainView extends UI implements XmlView { 


	private XmlController controller = new XmlController(this);
	private TreeTable tree;
	private HierarchicalContainer container;
	private VerticalLayout layout;
	private HorizontalLayout filePanel;
	private Button saveButton;
	private Button loadButton;
	private ComboBox fileList;
	private ContextMenu contextMenu;
	private Map <String, ActionType> actionMap = new HashMap<>();
	private int nodeCounter;
	private int selectedItemId;
	private final String ELEMENT_PROPERTY = "Element Name";
	private final String VALUE_PROPERTY = "Value";
	private final String EMPTY_FILENAME_NOTIFICATION = "File name can not be empty";
	private final String GENERATED_NAME = "NEW";
	
	
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = XmlMainView.class, widgetset = "com.xmlConfig.view.widgetset.FirstvaadWidgetset")
	public static class Servlet extends VaadinServlet {
	}
 
	@Override
	protected void init(VaadinRequest request) {	
		initComponents();
		addComponents();
		setComponentListeners();		
	}
	
	@Override
	public void displayFile(Document doc){
	    initTree();
	    Element root = doc.getDocumentElement();
        int parentId = nodeCounter;
        tree.addItem(getArrayOfTreeComponent(root.getNodeName(), root.getNodeValue()), parentId);
        addAttributesToTree(root, parentId);	   
        addChildrenToTree(root.getChildNodes(), parentId);
	}

	@Override
	public void showSaveSucces() {
		Notification.show("FILE SAVED");	
	}
	
	@Override
	public void showSaveFail() {
		Notification.show("CAN NOT SAVE FILE");		
	}
	
	@Override
	public void showLoadingFileFail() {
		Notification.show("CAN NOT LOAD FILE");
	}
	
	@Override
	public void showModificationFail() {
		Notification.show("CAN NOT MODIFY THAT VALUE");		
	}
	
	@Override
	public void createNewElement(){
		addNewItemToTree();
	}
	
	@Override
	public void createNewAttribute() {
		addNewItemToTree();
		tree.setChildrenAllowed(nodeCounter, false);		
	}
	
	@Override
	public void removeItem() {
		container.removeItemRecursively(selectedItemId);		
	}
	
	private void addNewItemToTree(){
		tree.addItem(getArrayOfTreeComponent(GENERATED_NAME, ""), ++nodeCounter);
		tree.setParent(nodeCounter, selectedItemId);
	}
	
	
	private void addComponents(){
		filePanel.addComponent(fileList);
	    filePanel.addComponent(loadButton);
	    filePanel.addComponent(saveButton); 
		layout.addComponent(filePanel);
	}
	
	private void initComponents(){
		List<String> names = controller.getFileList();
		
		fileList = new ComboBox();
		fileList.setInputPrompt("SELECT FILE");
		fileList.addItems(names);
		fileList.setValue(names.get(0));
		
		filePanel = new HorizontalLayout();
		layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout); 
			
		saveButton  = new Button("SAVE");
		saveButton.setEnabled(false);
		loadButton  = new Button("LOAD");
				
	}
	
	
	private void initTree(){
		if(tree != null)
			layout.removeComponent(tree);			
		container = new HierarchicalContainer();
		
		tree = new TreeTable("XML Configuration", container);
		tree.addContainerProperty(ELEMENT_PROPERTY, Component.class, null);
		tree.addContainerProperty(VALUE_PROPERTY, Component.class, null);
		tree.setWidth("400");
		tree.setEditable(true);
		tree.setImmediate(true);
				
		saveButton.setEnabled(true);
		actionMap.put(ELEMENT_PROPERTY, ActionType.CHANGE_NAME);
		actionMap.put(VALUE_PROPERTY, ActionType.CHANGE_VALUE);
		setTreeListener();
		initContextMenu();
		nodeCounter = 0;
		layout.addComponent(tree);	
	}
	
	private Command prepareCommand(){
		Command command = new Command();
		command.setItemId(selectedItemId);	
		return command;
	}

	private void addChildrenToTree(NodeList children, int id) {
	    if (children.getLength() > 0) {	
	    	int parentId = id;
	        for (int i = 0; i < children.getLength(); i++) {
	        	Node node = children.item(i);
	        	if(node instanceof Element){
	        		int childId = ++nodeCounter;	            	   
		            tree.addItem(getArrayOfTreeComponent(node.getNodeName(), node.getNodeValue()), childId);
		            tree.setParent(childId, parentId);
		            addAttributesToTree(node, childId);
		            addChildrenToTree(node.getChildNodes(), childId);
	        	}	        	
	        }
	    }
	}	
	
	private  void addAttributesToTree(Node node, int parentId){
		 if(node.hasAttributes()){
	        	NamedNodeMap attr = node.getAttributes();
	        	for(int j = 0; j < attr.getLength(); j++){
	        		int childId = ++nodeCounter;
	        		tree.addItem(getArrayOfTreeComponent(attr.item(j).getNodeName(), attr.item(j).getNodeValue()), childId);
	        		tree.setParent(childId, parentId);
	        		tree.setChildrenAllowed(childId, false);
	        	}
	        }
	}

	private Component[] getArrayOfTreeComponent(String name, String value){
		Label[] components = new Label[]{new Label(name), new Label(value)};
		setAsContextMenuOf(components);
		return components;			
	}
	
    private Component getTreeComponent(String value){
		Label component = new Label(value);
		contextMenu.setAsContextMenuOf(component);
		return component;		
	}
	
	private void setAsContextMenuOf(ContextClickNotifier[] components){
		for(ContextClickNotifier c: components)
			contextMenu.setAsContextMenuOf(c);				
	}

	private void setComponentListeners(){
		
		loadButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				try{
					fileList.validate();
					String fileName = (String)fileList.getValue();
					controller.getFile(fileName);  	
				}catch(InvalidValueException e){				
					Notification.show(EMPTY_FILENAME_NOTIFICATION);					
				}	
			}
		});
		
		saveButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				controller.saveFile();		
			}
		});
		
		fileList.addValidator(new StringLengthValidator(EMPTY_FILENAME_NOTIFICATION, 1, 15, false));
	}
	
	private void setTreeListener(){
			tree.addItemClickListener(new ItemClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
            public void itemClick(ItemClickEvent event) {
				
				int itemId = (int) event.getItemId();	
				if(event.getButton() == MouseButton.LEFT){										
		            int parentItemId = (itemId == 0) ? itemId : (int) tree.getParent(itemId);                               
		            String propertyName = (String) event.getPropertyId();
		            Property<Component> containerProperty = tree.getContainerProperty(itemId, propertyName);
		            Command command = new Command(actionMap.get(propertyName), itemId, parentItemId);
		            Component component = containerProperty.getValue();
		            		            	
		            if (component instanceof Label) 
		                 changeLabelToTextField((Label) component, containerProperty, command);	    				
				}
				else
					selectedItemId = itemId;
				                	                 										
            }			
		});	
	}
	
	private void changeLabelToTextField(Label label, final Property<Component> containerProperty, final Command command){
		
		final TextField field = new TextField();                      
        field.setImmediate(true);
        field.setValidationVisible(true);
        field.setValue((label.getValue() == null) ? "" : label.getValue());           
        containerProperty.setValue(field);
        field.focus(); 
        field.addBlurListener(new BlurListener() {
    	    
        	@Override
            public void blur(BlurEvent event) {
        		containerProperty.setValue(getTreeComponent(field.getValue()));
        		command.setNewValue(field.getValue());
        		controller.updateNameOrValue(command);            	
        	}
		});
	}
	
	private void initContextMenu() {
		contextMenu = new ContextMenu(tree, true);
		contextMenu.addItem("Add new element", new Menu.Command() {	
			
			@Override			
			public void menuSelected(MenuItem selectedItem) {
				controller.addElement(prepareCommand());			
			}
		});	
		
        contextMenu.addItem("Add new attribute", new Menu.Command() {	
        	
			@Override
			public void menuSelected(MenuItem selectedItem) {			
				controller.addAttribute(prepareCommand());		
			}
		});	
        
        contextMenu.addItem("Remove item", new Menu.Command() {	
        	
			@Override
			public void menuSelected(MenuItem selectedItem) {	
				controller.removeItem(selectedItemId);
			}		
		});	
	}

	
	
	

	


	



	
}