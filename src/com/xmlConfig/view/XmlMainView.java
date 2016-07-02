package com.xmlConfig.view;


import java.util.ArrayList;
import java.util.HashMap;
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
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
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
	private List<Integer> paramsItems = new ArrayList<>();
	private Map<String, ArrayList<Integer>> jsViewItems = new HashMap<>();
	private HorizontalSplitPanel horizontalSplit = new HorizontalSplitPanel();
	private VerticalSplitPanel verticalSplit = new VerticalSplitPanel();
	private JsViewUpdateService jsService;
	
	private int itemCounter;
	private int selectedItemId;
	
	private final String ELEMENT_PROPERTY = "Element Name";
	private final String VALUE_PROPERTY = "Value";
	private final String EMPTY_FILENAME_NOTIFICATION = "File name can not be empty";
	private final String GAUGE_PROPERTY = "Gauge";
	private final String GENERATED_NAME = "NEW";
	private final String INFO = "Left click - edit, Right click - context menu";
	private final String GEOMETRY = "Geometry";
	private final String PARAMS= "Params";
	private final String UNITS = "Units";
	private final String BOX = "Box";
	private final String WEDGE = "Wedge";
	private boolean hasError = false;
	
	
	
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
        getGauges();
        jsService.updateJsPanel();
	}

	@Override
	public void showMessage(String message){
		hasError = true;
		Notification.show(message);	
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
		  Component c = containerProperty.getValue();
		  if(c instanceof Label){
			  Label l = (Label)containerProperty.getValue();
			  l.setValue(gauge);	
		  } else
			  c.setCaption(gauge);
	}
	
	private void addNewItemToTree(){
		tree.addItem(getArrayOfTreeComponent(GENERATED_NAME, "", ""), ++itemCounter);
		tree.setParent(itemCounter, selectedItemId);
	}
	
	
	private void addComponents(){
		filePanel.addComponent(fileList);
	    filePanel.addComponent(loadButton);
	    filePanel.addComponent(saveButton); 
	    filePanel.setMargin(true);
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
		
		verticalSplit.setMaxSplitPosition(15f, Unit.PERCENTAGE);
		verticalSplit.setFirstComponent(filePanel);
		verticalSplit.setSecondComponent(horizontalSplit);
		horizontalSplit.setFirstComponent(layout);
		jsService = new JsViewUpdateService(horizontalSplit, filePanel);
		
		setContent(verticalSplit);	
		info = new Label(INFO);
		info.setVisible(false);
		saveButton = new Button("SAVE");
		saveButton.setEnabled(false);
		loadButton = new Button("LOAD");				
	}
		
	private void initTree(){
		if(tree != null){
			layout.removeComponent(tree);			
			paramsItems.clear();
			jsService.clear();
		}
		
		container = new HierarchicalContainer();	
		tree = new TreeTable("XML Configuration", container);
		tree.addContainerProperty(ELEMENT_PROPERTY, Component.class, null);
		tree.addContainerProperty(VALUE_PROPERTY, Component.class, null);
		tree.addContainerProperty(GAUGE_PROPERTY, Component.class, null);
		tree.setWidth("500");
		tree.setEditable(true);
		tree.setImmediate(true);
		tree.setPageLength(0);
		info.setVisible(true);
		saveButton.setEnabled(true);
		setTreeListener();
		initContextMenu();
		itemCounter = 0;
		jsViewItems.put(GEOMETRY, new ArrayList<Integer>());
		jsViewItems.put(BOX, new ArrayList<Integer>());
		jsViewItems.put(WEDGE, new ArrayList<Integer>());
		layout.addComponent(tree);	
		
		tree.setCellStyleGenerator( new Table.CellStyleGenerator(){

			@Override
			public String getStyle(Table source, Object itemId,
					Object propertyId) {
				if(paramsItems.contains(itemId))
					return "params";
		
				return null;
			}		
		});	
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
	        		int childId = ++itemCounter;    
	        		if(!node.getNodeName().equals(PARAMS)){        			
	        			jsService.addComponent((Element)node, childId);			
                        tree.addItem(getArrayOfTreeComponent(node.getNodeName(), node.getNodeValue(), ""), childId);
			            tree.setParent(childId, parentId);
			            addAttributesToTree(node, childId);
			            addChildrenToTree(node.getChildNodes(), childId);
	        		} else if(node.getParentNode().getNodeName().equals(UNITS))	        		
	        			addUnitsPanelToTree((Element)node, childId, parentId);	        			
	        		  else {        			
	        			addAttributesToTree(node, parentId);
			            addChildrenToTree(node.getChildNodes(), parentId);
	        		}	            
	        	}	        	
	        }
	    }
	}	

	private boolean isBoxViewItem(String name){
		return name.equals(GEOMETRY) ||
			   name.equals(BOX) ||
			   name.equals(WEDGE);
	}
	
	private  void addAttributesToTree(Node node, int parentId){
		 if(node.hasAttributes()){
	        	NamedNodeMap attr = node.getAttributes();
	        	for(int j = 0; j < attr.getLength(); j++){
	        		int childId = ++itemCounter;
	        	    tree.addItem(getArrayOfTreeComponent(attr.item(j).getNodeName(), attr.item(j).getNodeValue(), ""), childId);
	        		tree.setParent(childId, parentId);
	        		tree.setChildrenAllowed(childId, false);
	        		if(isBoxViewItem(node.getNodeName()))
	        			jsViewItems.get(node.getNodeName()).add(childId);
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
		paramsItems.add(nodeId);
		tree.addItem(getArrayOfTreeComponent(paramNode.getNodeName(), paramNode.getNodeValue(), element.getAttribute("gauge")), nodeId);
		tree.setParent(nodeId, parentId);
		tree.setChildrenAllowed(nodeId, false);
		itemCounter += 2;
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
				if(event.getButton() == MouseButton.LEFT){												                                       
		            String propertyName = (String) event.getPropertyId();
		            Property<Component> containerProperty = tree.getContainerProperty(selectedItemId, propertyName);
		            if(paramsItems.contains(selectedItemId)){
		            	 ActionType type = propertyName.equals(ELEMENT_PROPERTY) ? ActionType.CHANGE_UNIT_NAME :
		            		 			   propertyName.equals(VALUE_PROPERTY) ? ActionType.CHANGE_UNIT_VALUE : ActionType.CHANGE_UNIT_GAUGE;
		            	 Command command = new Command(type, selectedItemId);
		            	 Component component = containerProperty.getValue();
		            	 changeLabelToTextField((Label) component, containerProperty, command, propertyName);	    		
		            } else{
		            	 ActionType type = propertyName.equals(ELEMENT_PROPERTY) ? ActionType.CHANGE_NAME : ActionType.CHANGE_VALUE;
				         Command command = new Command(type, selectedItemId);
				         Component component = containerProperty.getValue();
				            		            	
				         if (component instanceof Label && !propertyName.equals(GAUGE_PROPERTY)) 
				              changeLabelToTextField((Label) component, containerProperty, command, propertyName);	    				
						}			                	                 										            	
		            }           	
            }			
		});	
	}
	
	private void changeLabelToTextField(Label label, final Property<Component> containerProperty,
													 final Command command,
													 final String propertyName){
		final TextField field = new TextField();                      
        field.setImmediate(true);
        field.setValidationVisible(true);
        field.setValue((label.getValue() == null) ? "" : label.getValue());           
        containerProperty.setValue(field);
        field.focus(); 
        field.addBlurListener(new BlurListener() {
    	    
        	@Override
            public void blur(BlurEvent event) {
        		command.setNewValue(field.getValue());       		
        	    if(paramsItems.contains(command.getItemId())){
        	    	controller.updateUnits(command);
        	    	getGauges();
        	    }
        	    else{
        	    	 if(propertyName.equals(VALUE_PROPERTY))
        	    		 jsService.updateComponent(getParentId(command.getItemId()), getItemName(command.getItemId()), command.getNewValue());
        	    	 controller.updateNameOrValue(command);         	    	
        	    }            	  
        	    containerProperty.setValue(getTreeComponent(field.getValue()));
        	    if(hasError);
        	    	//((Label)containerProperty.getValue()).setStyleName("error");
        	}
		});
	}
	
	@SuppressWarnings("unchecked")
	private String getItemName(int id){
		 Property<Component> containerProperty = tree.getContainerProperty(id, ELEMENT_PROPERTY);	
		 return ((Label)containerProperty.getValue()).getValue();
	}
	
	private int getParentId(int id){
		return (int)container.getParent(id);	
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
