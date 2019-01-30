package com.herve.ocgames.utils;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileTool {
    private static Logger supportLogger = Logger.getLogger("support_file");
    private static Logger devLogger = Logger.getLogger("development_file");

    /**
     *
     * Use try-without feature with autoclosable objet introduce with java 1.7
     * @param fileName
     * @return
     */
    public static ArrayList<String[]> getArrayListFromFile(String fileName){
        return new ArrayList<String[]>();
    }

    public static String[][] getArrayFromFile(String fileName){
        ArrayList<String[]> alProperties = getArrayListFromFile(fileName);
        return (String[][]) alProperties.toArray();
    }

    /**
     *
     * Use try-without feature with autoclosable objet introduce with java 1.7
     * @param fileName
     * @return
     */
    public static Map<String,String> getMapFromFile(String fileName){
        return new HashMap<>();
    }

}
