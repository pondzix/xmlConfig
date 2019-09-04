package com.xmlConfig.domain;

public class Command {

	private ActionType actionType;
	private int itemId;
	private String newValue;
	
	public Command() {
	}

	public Command(ActionType actionType, int itemId, int parentItemId,
			String newValue) {
		super();
		this.actionType = actionType;
		this.itemId = itemId;
		this.newValue = newValue;		
	}
	
	public Command(ActionType actionType, int itemId) {
		super();
		this.actionType = actionType;
		this.itemId = itemId;
	}

	public ActionType getActionType() {
		return actionType;
	}
	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	
	
}
