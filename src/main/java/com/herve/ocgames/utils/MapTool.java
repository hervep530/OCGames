package com.herve.ocgames.utils;

import com.herve.ocgames.core.exceptions.InvalidAliasKey;
import com.herve.ocgames.core.exceptions.InvalidArrayOption;
import com.herve.ocgames.core.exceptions.InvalidMapConverter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.HashMap;
import java.util.Map;

public class MapTool {

    private static final Logger supportLogger = LogManager.getLogger("support_file");
    private static final Logger devConsoleLogger = LogManager.getLogger("development_console");
    private static final Logger dev = LogManager.getLogger("development_file");
    private static boolean loggerInitialized = false;
    private static final Level VALUE = Level.getLevel("VALUE");
    private static final Level COMMENT = Level.getLevel("COMMENT");
    private static final Level LOOP = Level.getLevel("LOOP");
    private static Level debugVerbosity = Level.getLevel("WARN");

    /**
     * Get PropertyHelper core.debug value and if true, set debug level (VALUE / COMMENT / LOOP)
     */
    private static void initLogger(){
        Configurator.setLevel(dev.getName(), debugVerbosity);
        loggerInitialized = true;
    }

    /**
     * Convert a map<String, String> to bidimensionnal array String[][] (usefull to avoid usage of lambda)
     * @param inputMap a input map<string,string> to convert
     * @return array String[nbKey][2] as {{key,value},{key,value},....}
     */
    public static String[][] convertStringMapToStringArray(Map<String,String> inputMap, boolean verbose) {
        if ( ! loggerInitialized ) initLogger();

        if (inputMap == null) {
            if (verbose) dev.fatal("You can't use this method with null mapConverter");
            throw new InvalidMapConverter();
        }
        if (inputMap.isEmpty()) {
            if (verbose) dev.fatal("You can't use this method with empty mapConverter");
            throw new InvalidMapConverter();
        }
        int nbEntry = inputMap.size();
        String[][] outputArray = new String[nbEntry][2];
        int i = 0;
        for (Map.Entry<String,String> entry : inputMap.entrySet()){
            outputArray[i][0] = entry.getKey();
            outputArray[i][1] = entry.getValue();
            i++;
        }
        return outputArray;
    }

    /**
     * Convert a flat array String[] (like args[]) to a String map, with using a converter map<String alias, String key>
     * Use Diamond Type introduce with java 1.7
     * @param mapConverter map<String alias, String key> working as conversion rules
     * @param flatArray array containing suites of strings as : {alias1, value1, alias2, alias3, value 3,....}
     * @return a result map as {<key1, value1>,<key2, value2>,<key3, value3>,....}
     */
    public static Map<String,String> convertFlatStringArrayToMap(Map<String,String> mapConverter, String[] flatArray, boolean verbose) {
        if ( ! loggerInitialized ) initLogger();

        Map<String, String> mapOptions = new HashMap<>();
        String index = "";
        String value = "";
        String key = "";

        if (mapConverter == null) {
            dev.fatal("You can't use this method with null mapConverter");
            throw new InvalidMapConverter();
        }
        if (mapConverter.isEmpty()) {
            dev.fatal("You can't use this method with empty mapConverter");
            throw new InvalidMapConverter();
        }
        if (flatArray == null) {
            dev.error("Usage of this method with null flat array is not recommended");
            return  mapOptions;
        }
        if (flatArray.length < 1) {
            dev.debug("In method convertFlatStringArrayToMap, array option is empty.");
            return  mapOptions;
        }
        for (int i = 0; i < flatArray.length; i++) {
            if (flatArray[i] == null) {
                if (verbose) devConsoleLogger.fatal("Invalid array option");
                throw new InvalidArrayOption();
            }
            if (flatArray[i].startsWith("-")) {
                // si c'est une options (c'est à dire -qqchose ou --qqchose)
                if (! index.contentEquals("")) {
                    // si l'index n'a pas été utilisé (il est non ""), on le traite comme un binaire ("0", ou "1")
                    value = "1";
                    if (index.startsWith("no")) {
                        value = "0";
                        index = index.replaceAll("no", "");
                    }
                    if (mapConverter.containsKey(index)) {
                        mapOptions.put(mapConverter.get(index), value);
                    } else {
                        if (verbose) devConsoleLogger.fatal("Invalid option : " + index);
                        throw new InvalidAliasKey();
                    }
                }
                // Dans tous les cas on stocke l'option dans index
                index = flatArray[i].replaceAll("^-+", "");
            } else {
                // sinon il s'agit d'une valeur que l'on va associer à l'options stockée dans index
                if (! index.contentEquals("") && mapConverter.containsKey(index)) {
                    mapOptions.put(mapConverter.get(index), flatArray[i]);
                    index = "";
                } else {
                    if (verbose) devConsoleLogger.fatal("Invalid option : " + index);
                    throw new InvalidAliasKey();
                }
            }
        }
        if (! index.contentEquals("")) {
            // si l'index n'a pas été utilisé (il est non ""), on le traite comme un binaire ("0", ou "1")
            value = "1";
            if (index.startsWith("no")) {
                value = "0";
                index = index.replaceAll("no", "");
            }
            if (mapConverter.containsKey(index)) {
                mapOptions.put(mapConverter.get(index), value);
            } else  {
                if (verbose) devConsoleLogger.fatal("Invalid option : " + index);
                throw new InvalidAliasKey();
            }
        }
        return mapOptions;
    }

}
