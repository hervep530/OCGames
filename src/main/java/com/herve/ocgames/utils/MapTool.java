package com.herve.ocgames.utils;

import com.herve.ocgames.core.exceptions.InvalidAliasKey;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class MapTool {

    private static Logger supportLogger = Logger.getLogger("support_file");
    private static Logger devLogger = Logger.getLogger("development_file");

    public static String[][] convertStringMapToStringArray(Map<String,String> inputMap) {
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
     *
     * Use Diamond Type introduce with java 1.7
     * @param alias
     * @param arguments
     * @return
     */
    public static Map<String,String> convertFlatStringArrayToMap(Map<String,String> alias, String[] arguments) {
        Map<String, String> mapOptions = new HashMap<>();
        String index = "";
        String value = "";
        String key = "";

        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i].startsWith("-")) {
                // si c'est une options (c'est à dire -qqchose ou --qqchose)
                if (! index.contentEquals("")) {
                    // si l'index n'a pas été utilisé (il est non ""), on le traite comme un binaire ("0", ou "1")
                    value = "1";
                    if (index.startsWith("no")) {
                        value = "0";
                        index = index.replaceAll("no", "");
                    }
                    if (alias.containsKey(index)) {
                        mapOptions.put(alias.get(index), value);
                    } else throw new InvalidAliasKey();
                }
                // Dans tous les cas on stocke l'option dans index
                index = arguments[i].replaceAll("^-+", "");
            } else {
                // sinon il s'agit d'une valeur que l'on va associer à l'options stockée dans index
                if (! index.contentEquals("") && alias.containsKey(index)) {
                    mapOptions.put(alias.get(index), arguments[i]);
                    index = "";
                } else throw new InvalidAliasKey();
            }
        }
        if (! index.contentEquals("")) {
            // si l'index n'a pas été utilisé (il est non ""), on le traite comme un binaire ("0", ou "1")
            value = "1";
            if (index.startsWith("no")) {
                value = "0";
                index = index.replaceAll("no", "");
            }
            if (alias.containsKey(index)) {
                mapOptions.put(alias.get(index), value);
            } else throw new InvalidAliasKey();
        }
        return mapOptions;
    }

}
