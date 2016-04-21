package com.xmlConfig.service;

import com.xmlConfig.domain.ItemDTO;
import com.xmlConfig.exception.IllegalFileModification;

public interface UpdateManager {

	public void update(ItemDTO item) throws IllegalFileModification;
}
