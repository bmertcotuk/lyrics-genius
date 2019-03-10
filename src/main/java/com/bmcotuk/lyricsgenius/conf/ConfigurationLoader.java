package com.bmcotuk.lyricsgenius.conf;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Author: B. Mert Cotuk
 * Date:   08.01.2019
 * Time:   17:12
 * https://github.com/bmertcotuk
 */
public class ConfigurationLoader {

    private static final Logger              logger = Logger.getLogger(ConfigurationLoader.class);
    private static       String              configurationFileName;
    private static       Map<String, String> propertyMap;
    private static       InputStream         inputStream;

    private ConfigurationLoader() {
    }

    public static void initialize(String language) throws IOException {

        try {
            logger.debug("Initializing the configuration...");
            configurationFileName = "configuration_" + language + ".properties";
            Properties properties = new Properties();
            propertyMap = new HashMap<>();
            inputStream = ConfigurationLoader.class.getClassLoader().getResourceAsStream(configurationFileName);

            if (inputStream == null)
                throw new FileNotFoundException("Property file " + configurationFileName + " not found in the class classpath.");

            properties.load(inputStream);

            propertyMap = properties.entrySet().stream().collect(
                    Collectors.toMap(
                            e -> e.getKey().toString(),
                            e -> e.getValue().toString()
                    )
            );
            logger.debug("Initialized the configuration.");
        } catch (FileNotFoundException e) {
            logger.error("Initialization failed: ", e);
        } finally {
            if (inputStream != null)
                inputStream.close();
        }

    }

    public static Integer getInteger(String key) {
        return Integer.valueOf(propertyMap.get(key));
    }

    public static String getString(String key) {
        return propertyMap.get(key);
    }

}
