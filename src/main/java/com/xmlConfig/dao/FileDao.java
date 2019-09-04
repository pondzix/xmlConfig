package com.xmlConfig.dao;

import com.xmlConfig.domain.FileAdapter;
import com.xmlConfig.exception.IllegalFileModification;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class FileDao {

    private String directoryPath;
    private String extension;

    FileDao() {
        setDirectoryPath();
        extension = getExtension();
    }

    public abstract void saveFile(FileAdapter fileModel) throws ParserConfigurationException, TransformerException, IllegalFileModification;

    public abstract String getExtension();

    public File getFile(String fileName){
        return new File(directoryPath + fileName + extension);
    }

    public List<String> getFileList(){
        List<String> fileNameList = new ArrayList<>();
        File directory = new File(getDirectoryPath());
        File[] fileList = directory.listFiles();
        for(File f: fileList)
            if(f.getName().endsWith(extension))
                fileNameList.add(f.getName().replace(extension, ""));

        return fileNameList;
    }

    private void setDirectoryPath(){
        Properties properties = new Properties();
        String propertiesName = "directory.properties";
        ClassLoader loader = getClass().getClassLoader();
        InputStream inputStream = loader.getResourceAsStream(propertiesName);
        try {
            properties.load(inputStream);
            directoryPath = properties.getProperty("directoryPath");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String getDirectoryPath() {
        return directoryPath;
    }
}
