package com.pack.saviynt.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * The Class ObjectReader.
 */
public class ObjectReader {

    /** The logger. */
    static Logger logger = Logger.getLogger(AppData.class.getName());

    /** The objects. */
    public static HashMap<String, Object> objects = null;

    static {
        /** The br. */
        BufferedReader br = null;
        if (objects == null) {
            objects = new HashMap<>();
            try {
                String csvSplitBy = AppData.getProperty("csvSplitBy");
                String[] files = AppData.getProperty("ObjectRepository").split(";");

                for (String file : files) {
                    logger.info("Fetching Object properties from " + file + " ...");
                    String line = "";
                    br = new BufferedReader(
                            new FileReader(System.getProperty("user.dir") + "/src/test/resources/" + file));

                    String[] colHeader = br.readLine().split(csvSplitBy);
                    while ((line = br.readLine()) != null) {
                        String[] rows = line.split(csvSplitBy);
                        for (int i = 3; i < rows.length; i++) {
                            objects.put(
                                    rows[2].trim().toUpperCase().replaceAll(" ", "_") + "_"
                                            + colHeader[i].trim().toUpperCase().replaceAll(" ", "_"),
                                    rows[i].replace("\"", "'").replace("$", ","));
                        }
                    }
                }

            } catch (Exception e) {
                logger.warning(e.getMessage());
            }
        }
    }

    /**
     * Gets the property.
     * @param property the property
     * @return the property
     */
    public static String getProperty(String property) {
        try {
            return objects.get(property.toUpperCase().trim().replaceAll(" ", "_").toUpperCase()).toString();
        } catch (Exception e) {
            return "";
        }
    }
}
