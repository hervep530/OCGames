package com.herve.ocgames.utils;

import com.herve.ocgames.core.PropertyHelper;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileTool {
    private static Logger supportLogger = Logger.getLogger("support_file");
    private static Logger devLogger = Logger.getLogger("development_file");

    /**
     * Get a properties file in arrayList of String Array of 2 elements (one is the key, the 2nd is value)
     * Use try-with-resources feature with autoclosable objet introduce with java 1.7
     * @param fileName
     * @return
     */
    public static ArrayList<String[]> getArrayListFromFile(String fileName){
        ArrayList<String[]> properties = new ArrayList<String[]>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            String[][] substitutions = {{"\\\\#","VAR_DIESE"},
                    {"\\\\=","VAR_EQUAL"},
                    {"(\\s*[#].*|^\\s*|\\s*$)",""},
                    {"\\s*=\\s*","="},
                    {"VAR_EQUAL", "="},
                    {"VAR_DIESE", "#"}};
            while ((line = br.readLine()) != null) {
                String keyVal = StringTool.arrayReplace(line,substitutions);
                if (StringTool.match(keyVal,".*=\".*\"$")) {
                    String[][] substitutions2 = {{"=\"","="}, {"\"$",""}};
                    keyVal = StringTool.arrayReplace(keyVal,substitutions2);
                }
                if (StringTool.match(keyVal, "[a-zA-Z0-9.-]{3,}=.*\\w.*")) {
                    String[] propertyImport = new String[2];
                    propertyImport[0] = StringTool.arrayReplace(keyVal, new String[][] {{"=.*$",""}});
                    propertyImport[1] = StringTool.arrayReplace(keyVal, new String[][] {{"^[a-zA-Z0-9.-]{3,}=",""}});
                    properties.add(propertyImport);
                    // properties.add(keyVal.split("="));
                }
            }
        } catch (FileNotFoundException e) {
            supportLogger.warn("File " + fileName + " doesn't exist.");
            return null;
        } catch (IOException e) {
            supportLogger.warn("Can't access to file " + fileName + ".");
            return null;
        }
        return properties;
    }

    public static String[][] getArrayFromFile(String fileName){
        ArrayList<String[]> alProperties = getArrayListFromFile(fileName);
        return (String[][]) alProperties.toArray();
    }

    /**
     *
     * Use try-with-resources feature with autoclosable objet introduce with java 1.7
     * @param fileName
     * @return
     */
    public static Map<String,String> getMapFromFile(String fileName){
        Map<String, String> properties = new HashMap<>();
        String[][] filenameReplace = new String[][] {{"VAR_NAME",fileName}};
        // Using of try-without with autoclosable objet introduce with java 1.7
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            String[][] substitutions = {{"\\\\#","VAR_DIESE"},
                    {"\\\\=","VAR_EQUAL"},
                    {"(\\s*[#].*|^\\s*|\\s*$)",""},
                    {"\\s*=\\s*","="},
                    {"VAR_EQUAL", "="},
                    {"VAR_DIESE", "#"}};
            while ((line = br.readLine()) != null) {
                String keyVal = StringTool.arrayReplace(line,substitutions);
                if (StringTool.match(keyVal,".*=\".*\"$")) {
                    String[][] substitutions2 = {{"=\"","="}, {"\"$",""}};
                    keyVal = StringTool.arrayReplace(keyVal,substitutions2);
                }
                if (StringTool.match(keyVal, "[a-zA-Z0-9.-]{3,}=.*\\w.*")) {
                    String[] property = new String[2];
                    property[0] = StringTool.arrayReplace(keyVal, new String[][] {{"=.*$",""}});
                    property[1] = StringTool.arrayReplace(keyVal, new String[][] {{"^[a-zA-Z0-9.-]{3,}=",""}});
                    properties.put(property[0],property[1]);
                }
            }
        } catch (FileNotFoundException e) {
            supportLogger.warn("File " + fileName + " doesn't exist.");
            //supportLogger.warn(PropertyHelper.language("file.notFound", filenameReplace));
            return null;
        } catch (IOException e) {
            supportLogger.warn("Can't access to file " + fileName + ".");
            //supportLogger.warn(PropertyHelper.language("file.noAccess", filenameReplace));
            return null;
        }
        return properties;
    }

}
