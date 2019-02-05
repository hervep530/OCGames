package com.herve.ocgames.core;

import com.herve.ocgames.core.enums.ConfigEntry;
import com.herve.ocgames.core.enums.ConfigMode;
import com.herve.ocgames.core.enums.GameFromList;
import com.herve.ocgames.core.enums.GameVersion;
import com.herve.ocgames.utils.FileTool;
import com.herve.ocgames.utils.MapTool;
import com.herve.ocgames.utils.StringTool;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.herve.ocgames.Main.*;

public class PropertyHelper {
    private static HashMap<String,String> configRepository = new HashMap<String,String>();
    private static HashMap<String,String> languageRepository = new HashMap<String,String>();

    private static boolean debug = false;
    private static Level VALUE = Level.getLevel("VALUE");
    private static Level COMMENT = Level.getLevel("COMMENT");
    private static Level LOOP = Level.getLevel("LOOP");
    private static Level debugVerbosity = Level.getLevel("VALUE");
    private static final Logger dev = LogManager.getLogger(PropertyHelper.class.getName());

    /**
     * For this static classe replace a constructor
     */
    public static void initialize(String[] cmdLineOptions){
        defaultConfig();
        loadConfigFiles();
        importCommandLineOptions(cmdLineOptions);
        initLogger();
        loadLanguageFiles();
    }

    /**
     * For this static classe replace a constructor - Get default from an external HashMap instead of code below
     */
    public static void initialize(HashMap<String,String> defaultProperties, String[] cmdLineOptions) {
        defaultConfig();                                        // hard parameters in the code
        defaultConfig(defaultProperties);                       // additionnal parameters given in argument as Map
        loadConfigFiles();                                      // parameters from resource file in core package
        importCommandLineOptions(cmdLineOptions);         // parameters from command line
        initLogger();
        loadLanguageFiles();                                    // languages from resource file in core package
    }

    /**
     * Get PropertyHelper core.debug value and if true, set debug level (VALUE / COMMENT / LOOP)
     */
    private static void initLogger(){
        debug = StringTool.match(PropertyHelper.config("core.debug"), "^([Tt]rue|[Yy]es|1)$");
        // Here can be change debug verbosity (... < INFO < DEBUG < VALUE < COMMENT < LOOP < TRACE) - no debug = WARN
        if (debug) Configurator.setLevel(dev.getName(), debugVerbosity);
    }

    /**
     * Kind of getter for configReposity - get all the map configRepository
     */
    public static Map<String,String> config(){
        return configRepository;
    }

    /**
     * Kind of getter for configReposity - get only value for given key
     * @param key name of the config key
     * @return the value or "" if key doesn't exist
     */
    public static String config(String key){
        String value = "";
        if (configRepository.containsKey(key))
            value = configRepository.get(key);
        else
            supportLogger.warn("PropertyHelper : key \"" + key + "\" doesn't exist in configRepository");
        return value;
    }

    /**
     * Kind of getter for languageReposity - get all the Map languageRepository for read only
     */
    public static Map<String,String> language(){
        return languageRepository;
    }

    /**
     * Kind of getter for languageReposity - get only value for given key
     * @param key name of the language key
     * @return the value or "" if key doesn't exist
     */
    public static String language(String key){
        String value = "";
        if (languageRepository.containsKey(key))
            value = languageRepository.get(key);
        else
            supportLogger.warn("PropertyHelper : key \"" + key + "\" doesn't exist in languageRepository");
        return value;
    }

    public static String language(String key, String[][] substitutions){
        if ( ! languageRepository.containsKey(key) ){
            supportLogger.error("PropertyHelper can't find language entry (message) with key " + key);
            return "";
        }
        String message = languageRepository.get(key);
        message = StringTool.arrayReplace(message, substitutions);
        return message;
    }

    /**
     * Load a default config to prevent lack of parameters
     */
    private static void defaultConfig() {
        // ConfigMode.STRICT is a config method locked with enums, ConfigMode.CUSTOM make parameters free and independent
        configRepository.put("config.mode", ConfigMode.CUSTOM.toString());   // hard parameter (no change with file)
        // core parameters
        configRepository.put("core.debug", "0");
        configRepository.put("core.language", "default");
        // colors
        configRepository.put("color.rules", "blue");
        configRepository.put("color.defender", "cyan");
        configRepository.put("color.challenger", "purple");
        configRepository.put("color.winner", "blue");
        configRepository.put("color.looser", "red");
        // default - plusmoins.config = "pm40x"
        configRepository.put("plusmoins.version", "pm40x");
        configRepository.put("plusmoins.codeLength", "4");
        configRepository.put("plusmoins.digitsInGame", "10");
        configRepository.put("plusmoins.digitMaxRepeat", configRepository.get("plusmoins.codeLength"));
        configRepository.put("plusmoins.attempts", "6");

        // default - plusmoins.config = "pm46x"
        configRepository.put("mastermind.version", "mm462");
        configRepository.put("mastermind.codeLength", "4");
        configRepository.put("mastermind.digitsInGame", "6");
        configRepository.put("mastermind.digitMaxRepeat", configRepository.get("mastermind.codeLength"));
        configRepository.put("mastermind.attempts", "6");
    }

    /**
     * Load default config from Hashmap
     * @param defaultProperties HashMap contenant les propriétés par défaut
     */
    private static void defaultConfig(HashMap<String,String> defaultProperties) {
        configRepository = new HashMap<>(defaultProperties);
    }

    /**
     * Copy all config entries From gameName+".*" to "game.*" - allow to use a generic property (usefull but not mandatory)
     * @param gameName name of the game as found in property core.games
     */
    public static void loadGame(String gameName){
        try {
            GameFromList game = GameFromList.valueOf(gameName.toUpperCase());
            String version = configRepository.get(game.getName() + ".version");
            GameVersion loadingGame = GameVersion.valueOf(version.toUpperCase());
            configRepository.put("game.name", game.getName());
            configRepository.put("game.version", loadingGame.version());
            configRepository.put("game.primaryStrategy", loadingGame.getPrimaryStrategy());
            configRepository.put("game.codeLength", loadingGame.getCodeLength());
            configRepository.put("game.digitsInGame", loadingGame.getDigitsInGame());
            configRepository.put("game.digitMaxRepeat", loadingGame.getDigitsMaxRepeat());
            configRepository.put("game.attempts", config(game.getName() + ".attempts"));
        } catch (NullPointerException e) {
            consoleLogger.fatal("PropertyHelper - loadGame failed");
            return;
        }
    }

    /**
     * Copy all config entries From gameName+".*" to "game.*" - allow to use a generic property (usefull but not mandatory)
     * @param gameName name of the game as found in property core.games
     * @param configMode enum STRICT or CUSTOM
     */
    public static void loadGame(String gameName, ConfigMode configMode){
        if (configMode == ConfigMode.STRICT) {
            dev.log(COMMENT,"Configuration will be apply as defined in enum GameVersion - if failed keep default config");
            try {
                GameFromList game = GameFromList.valueOf(gameName.toUpperCase());
                String version = configRepository.get(game.getName() + ".version");
                GameVersion loadingGame = GameVersion.valueOf(version.toUpperCase());
                configRepository.put("game.name", game.getName());
                configRepository.put("game.version", loadingGame.version());
                configRepository.put("game.primaryStrategy", loadingGame.getPrimaryStrategy());
                configRepository.put("game.codeLength", loadingGame.getCodeLength());
                configRepository.put("game.digitsInGame", loadingGame.getDigitsInGame());
                configRepository.put("game.digitMaxRepeat", loadingGame.getDigitsMaxRepeat());
                configRepository.put("game.attempts", config(game.getName() + ".attempts"));
            } catch (NullPointerException e) {
                consoleLogger.fatal("PropertyHelper - loadGame failed");
                return;
            }
        } else {
            dev.log(COMMENT,"Can use all parameters defined in ConfigEntry - if failed keep default config");
            try {
                GameFromList game = GameFromList.valueOf(gameName.toUpperCase());
                configRepository.put("game.name", game.getName());
                configRepository.put("game.codeLength", configRepository.get(game.getName() + ".codeLength"));
                configRepository.put("game.digitsInGame", configRepository.get(game.getName() + ".digitsInGame"));
                if (configRepository.containsKey(game.getName() + ".digitMaxRepeat")) {
                    configRepository.put("game.digitMaxRepeat", configRepository.get(game.getName() + ".digitMaxRepeat"));
                    dev.log(VALUE,"digitMaxRepeat (found) = " + configRepository.get("game.digitMaxRepeat"));
                } else {
                    dev.log(COMMENT,"Key digitMaxRepeat not found in configRepository - will be same as codeLength");
                    configRepository.put("game.digitMaxRepeat", configRepository.get(game.getName() + ".codeLength"));
                    dev.log(VALUE,"digitMaxRepeat (not found) = " + configRepository.get("game.digitMaxRepeat"));
                }
                configRepository.put("game.attempts", configRepository.get(game.getName() + ".attempts"));
            } catch (NullPointerException e) {
                consoleLogger.fatal("PropertyHelper - loadGame failed");
                return;
            }
        }
    }

    private static void loadConfigFiles(){
        // Import file resources/config.properties following rules defined by enum ConfigEntry
        String fileName = "resources/config.properties";
        try {
            dev.log(COMMENT,"Use FileTool to import file and ConfigEntry enums to filter keys and values");
            FileTool.getArrayListFromFile(fileName).stream()
                    .filter(property->StringTool.match(property[1],
                            ConfigEntry.valueOf(property[0].replaceAll("\\.","").toUpperCase()).valueFilter()))
                    .forEach(property->configRepository.put(property[0],property[1]));
        } catch (NullPointerException e){
            // if file is not available FileTool warn and return null, so we catch NullPointer with debug
            supportLogger.warn("PropertyHelper : file " + fileName + " not found!");
        } catch ( EnumConstantNotPresentException | IllegalArgumentException e){
            // if file is not available FileTool warn and return null, so we catch NullPointer with debug
            supportLogger.warn("Invalid configuration in file" + fileName + "! default parameters will be applied.");
        } catch ( ArrayIndexOutOfBoundsException e) {
            // if file is not available FileTool warn and return null, so we catch NullPointer with debug
            supportLogger.warn("Invalid language entry in file " + fileName + "!");
        }

    }

    private static void loadLanguageFiles(){
        // Import file resources/language/language-CONFIG.properties (CONFIG is restricted with ConfigEntry)
        String fileName = "resources/language/language-" + configRepository.get("core.language") + ".properties";
        try {
            dev.log(COMMENT,"Use FileTool to import file and ConfigEntry with a permissive filter");
            String filterKey = ".*";
            FileTool.getArrayListFromFile(fileName).stream()
                    .filter(property->StringTool.match(property[0],filterKey))
                    .forEach(property->languageRepository.put(property[0],property[1]));
        } catch (NullPointerException e){
            // if file is not available FileTool warn and return null, so we catch NullPointer with debug
            supportLogger.warn("PropertyHelper : fichier " + fileName + " non trouvé!");
        } catch ( ArrayIndexOutOfBoundsException e) {
            // if the language property has no value
            supportLogger.warn("Invalid language entry in file " + fileName + "!");
        }
    }

    /**
     * Import command line options
     * Use lambda features (Stream.Collectors) introduce with java 1.8
     * @param arguments array of arguments (String[] args from Main)
     */
    public static void importCommandLineOptions(String[] arguments) {
        // Create Map to reference all key aliases
        if (arguments == null || arguments.length < 1 || arguments[0].contentEquals("")) {
            supportLogger.warn("PropertyHelper : No command line option!");
            return;
        }
        Map<String,String> validCmdLineAlias = new HashMap<>();
        validCmdLineAlias.put("debug", "debug");
        validCmdLineAlias.put("d", "debug");
        // Using ALMTool method, Create Map from arguments crossed with Map of alias, then overload properties in foreach
        MapTool.convertFlatStringArrayToMap(validCmdLineAlias, arguments, false)
                .forEach((k,v)->configRepository.put(k, v));
    }

    /**
     * Display list of properties from HashMap
     */
    public static void displayProperties(){
        String message = "";
        for (Map.Entry<String, String> entry : configRepository.entrySet()) {
            message += entry.getKey() + " : " + entry.getValue() + "%n";
        }
        for (Map.Entry<String, String> entry : languageRepository.entrySet()) {
            message += entry.getKey() + " : " + entry.getValue() + "%n";
        }
        System.out.println(String.format(message));
    }

    /**
     * Display a sorted list of properties filtered on key
     * @param filterKey string to filter key with regex
     */
    public static void displayProperties(String filterKey){
        // Filter and Sort
        Map<String,String> sortedProperties;
        if ( ! filterKey.contentEquals("") ) {
            // If String is not null, we use it on lambda to filter key and sort with TreeMap
            sortedProperties = new TreeMap<>(
                    Stream.concat(configRepository.entrySet().stream(),languageRepository.entrySet().stream())
                            .filter(x -> StringTool.match(x.getKey(), filterKey))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
            );
        } else {
            // else we only sort we TreeMap
            sortedProperties = new TreeMap<>(
                    Stream.concat(configRepository.entrySet().stream(),languageRepository.entrySet().stream())
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
            );
        }
        // We prepare String to display
        String message = "";
        for (Map.Entry<String, String> entry : sortedProperties.entrySet()) {
            message += entry.getKey() + " : " + entry.getValue() + "%n";
        }
        // and display
        System.out.println(String.format(message));
    }

    /**
     * Given a key from languageRepository, get and display the message
     */
    public static void displayMessage(String keyMessage){
        if ( ! languageRepository.containsKey(keyMessage) ){
            supportLogger.error("PropertyHelper can't find language entry (message) with key " + keyMessage);
            return;
        }
        String message = languageRepository.get(keyMessage);
        System.out.println(String.format(message));
    }

    /**
     * Given a key from languageRepository, get the matching message, do some subsitutions and display it
     * @param keyMessage key like "language.mymessage" as found in language file
     * @param substitutions array in 2 dimensions which contains {{search1,replacement1}, {search2, replacement2}, ...}
     */
    public static void displayMessage(String keyMessage, String[][] substitutions){
        if ( ! languageRepository.containsKey(keyMessage) ){
            supportLogger.error("PropertyHelper can't find language entry (message) with key " + keyMessage);
            return;
        }
        String message = languageRepository.get(keyMessage);
        message = StringTool.arrayReplace(message, substitutions);
        System.out.println(String.format(message));
    }

}
