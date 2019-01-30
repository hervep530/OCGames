package com.herve.ocgames.utils;

import org.apache.log4j.Logger;

import java.util.Map;

public class MapTool {

    private static Logger supportLogger = Logger.getLogger("support_file");
    private static Logger devLogger = Logger.getLogger("development_file");

    public static String[][] convertStringMapToStringArray(Map<String,String> inputMap) {
        return new String[][]{{"",""}};
    }

    /**
     *
     * Use Diamond Type introduce with java 1.7
     * @param alias
     * @param arguments
     * @return
     */
    public static Map<String,String> convertFlatStringArrayToMapWithCrossMap(Map<String,String> alias, String[] arguments) {
        return alias;
    }

}
