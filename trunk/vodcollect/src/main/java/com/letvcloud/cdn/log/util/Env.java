package com.letvcloud.cdn.log.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 */
public class Env {

    private static Properties prop = getProperties();

    public static String get(String key) {
        return prop.getProperty(key);
    }

    public static Properties getProperties() {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = Env.class.getClassLoader().getResourceAsStream("env.properties");
            properties.load(inputStream);
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return properties;
    }
}
