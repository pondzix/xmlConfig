package com.xmlConfig.domain;

public class ItemDTO {

	private String propertyName;
	private int itemId;
	private int parentItemId;
	private String newValue;
	
	

	public ItemDTO(String propertyName, int itemId, int parentItemId,
			String newValue) {
		this.propertyName = propertyName;
		this.itemId = itemId;
		this.parentItemId = parentItemId;
		this.newValue = newValue;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getParentItemId() {
		return parentItemId;
	}

	public void setParentItemId(int parentItemId) {
		this.parentItemId = parentItemId;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}  
}
