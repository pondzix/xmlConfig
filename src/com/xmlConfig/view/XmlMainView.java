package com.xmlConfig.view;


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
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ContextClickEvent.ContextClickNotifier;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
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
	private Label info;
	private ComboBox fileList;
	private ContextMenu contextMenu;
	
	private int itemCounter;
	private int selectedItemId;
	private int parentOfSelectedItem;
	
	private final String ELEMENT_PROPERTY = "Element Name";
	private final String VALUE_PROPERTY = "Value";
	private final String EMPTY_FILENAME_NOTIFICATION = "File name can not be empty";
	private final String GAUGE_PROPERTY = "Gauge";
	private final String GENERATED_NAME = "NEW";
	private final String INFO = "Left click - edit, Right click - context menu";
	
	
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
        int parentId = itemCounter;
        tree.addItem(getArrayOfTreeComponent(root.getNodeName(), root.getNodeValue(), ""), parentId);
        addAttributesToTree(root, parentId);	   
        addChildrenToTree(root.getChildNodes(), parentId);
        //getGauges();
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
		tree.setChildrenAllowed(itemCounter, false);		
	}
	
	@Override
	public void removeItem() {
		container.removeItemRecursively(selectedItemId);		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void updateGauge(int id, String gauge) {
		  Property<Component> containerProperty = tree.getContainerProperty(id, GAUGE_PROPERTY);
		  Label l = (Label)containerProperty.getValue();
		  l.setValue(gauge);		
	}
	
	private void addNewItemToTree(){
		tree.addItem(getArrayOfTreeComponent(GENERATED_NAME, "", ""), ++itemCounter);
		tree.setParent(itemCounter, selectedItemId);
	}
	
	
	private void addComponents(){
		filePanel.addComponent(fileList);
	    filePanel.addComponent(loadButton);
	    filePanel.addComponent(saveButton); 
		layout.addComponent(filePanel);
		layout.addComponent(info);
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
		ThemeResource res = new ThemeResource("index.html");
		BrowserFrame frame = new BrowserFrame("Static", res);
		frame.setWidth("100%");
		frame.setHeight("100%");
	

		HorizontalSplitPanel split = new HorizontalSplitPanel();
		split.setFirstComponent(layout);
		split.setSecondComponent(frame);
		
		setContent(split);
		//Label myLabel = new Label("<center><p style=\"background-color:rgb(55,55,55);font-size:50px;color:rgb(255,255,255)\">LABEL</p><center>",Label.CONTENT_RAW);
		info = new Label("<center><p style=\"background-color:rgb(55,55,55);font-size:50px;color:rgb(255,255,255)\">LABEL</p><center>",Label.CONTENT_RAW);
		info.setStyleName("mystyle");
		info.setVisible(false);
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
		tree.addContainerProperty(GAUGE_PROPERTY, Component.class, null);
		tree.setWidth("500");
		tree.setEditable(true);
		tree.setImmediate(true);
		
		info.setVisible(true);
		saveButton.setEnabled(true);
		setTreeListener();
		initContextMenu();
		itemCounter = 0;
		layout.addComponent(tree);	
	}
	
	private Command prepareCommand(){
		Command command = new Command();
		command.setItemId(selectedItemId);	
		command.setParentItemId(parentOfSelectedItem);
		return command;
	}

	private void addChildrenToTree(NodeList children, int id) {
	    if (children.getLength() > 0) {	
	    	int parentId = id;
	        for (int i = 0; i < children.getLength(); i++) {
	        	Node node = children.item(i);
	        	if(node instanceof Element){
	        		int childId = ++itemCounter;    
	        		if(!node.getNodeName().equals("Params")){
	        			tree.addItem(getArrayOfTreeComponent(node.getNodeName(), node.getNodeValue(), ""), childId);
			            tree.setParent(childId, parentId);
			            addAttributesToTree(node, childId);
			            addChildrenToTree(node.getChildNodes(), childId);
	        		} else if(node.getParentNode().getNodeName().equals("Units"))
	        			addUnitsPanelToTree((Element)node, childId, parentId);	        			
	        		  else {
	        			addAttributesToTree(node, parentId);
			            addChildrenToTree(node.getChildNodes(), parentId);
	        		}
	        		
		            
	        	}	        	
	        }
	    }
	}	
	
	private  void addAttributesToTree(Node node, int parentId){
		 if(node.hasAttributes()){
	        	NamedNodeMap attr = node.getAttributes();
	        	for(int j = 0; j < attr.getLength(); j++){
	        		int childId = ++itemCounter;
	        		tree.addItem(getArrayOfTreeComponent(attr.item(j).getNodeName(), attr.item(j).getNodeValue(), ""), childId);
	        		tree.setParent(childId, parentId);
	        		tree.setChildrenAllowed(childId, false);
	        	}
	        }
	}
	
	private void addUnitsPanelToTree(Element element, int nodeId, int parentId){
		NamedNodeMap attr = element.getAttributes();
		Node paramNode = null;
		for(int j = 0; j < attr.getLength(); j++)
			if(!attr.item(j).getNodeName().equals("gauge")){
				paramNode = attr.item(j);
				break;
			}
		tree.addItem(getArrayOfTreeComponent(paramNode.getNodeName(), paramNode.getNodeValue(), element.getAttribute("gauge")), nodeId);
		tree.setParent(nodeId, parentId);
		tree.setChildrenAllowed(nodeId, false);
	}

	private Component[] getArrayOfTreeComponent(String name, String value, String gauge){
		Label[] components = new Label[]{new Label(name), new Label(value), new Label(gauge)};
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
				
				selectedItemId = (int) event.getItemId();	
				parentOfSelectedItem = (selectedItemId == 0) ? selectedItemId : (int) tree.getParent(selectedItemId);   
				if(event.getButton() == MouseButton.LEFT){												                                       
		            String propertyName = (String) event.getPropertyId();
		            Property<Component> containerProperty = tree.getContainerProperty(selectedItemId, propertyName);
		            ActionType type = propertyName.equals(ELEMENT_PROPERTY) ? ActionType.CHANGE_NAME : ActionType.CHANGE_VALUE;
		            Command command = new Command(type, selectedItemId, parentOfSelectedItem);
		            Component component = containerProperty.getValue();
		            		            	
		            if (component instanceof Label && !propertyName.equals(GAUGE_PROPERTY)) 
		                 changeLabelToTextField((Label) component, containerProperty, command);	    				
				}			                	                 										
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
				controller.removeItem(prepareCommand());
			}		
		});	
           
	}
	
	 private void getGauges(){
		 Map<Integer, String> gauges = controller.getGauges();
		 for(Map.Entry<Integer, String> entry: gauges.entrySet())
			 updateGauge(entry.getKey(), entry.getValue());			 
	 }
}