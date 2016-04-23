package com.xmlConfig.service;

import com.xmlConfig.domain.Command;
import com.xmlConfig.exception.IllegalFileModification;

public interface UpdateManager {

	public void update(Command item) throws IllegalFileModification;
}
