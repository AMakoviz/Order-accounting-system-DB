package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ApplicationProperties {

    private Properties properties;

    private static ApplicationProperties appProperties;

    private ApplicationProperties() {
        properties = new Properties();
        File propFile = new File("application.properties");
        try {
            if (!propFile.exists()) {
                propFile.createNewFile();
            }
            properties.load(new FileInputStream(propFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUrl(){
        return properties.getProperty("database_url");
    }

    public String getUser(){
        return properties.getProperty("database_username");
    }

    public String getPassword(){
        return properties.getProperty("database_password");
    }

    public static ApplicationProperties getInstance(){
        if(appProperties==null) appProperties = new ApplicationProperties();
        return appProperties;
    }

}
