package com.xmlConfig.dao;


import com.xmlConfig.domain.FileAdapter;
import com.xmlConfig.exception.IllegalFileModification;


public class YamlFileDaoImpl extends FileDao{

    @Override
    public void saveFile(FileAdapter fileModel) throws IllegalFileModification {
        throw new IllegalFileModification("Can not save .yaml file");
    }

    @Override
    public String getExtension() {
        return ".yml";
    }
}
