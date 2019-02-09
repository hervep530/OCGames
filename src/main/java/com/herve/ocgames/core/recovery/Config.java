package com.herve.ocgames.core.recovery;

import com.herve.ocgames.core.PropertyHelper;
import com.herve.ocgames.core.enums.ConfigMode;
import com.herve.ocgames.utils.StringTool;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Config {
    private static HashMap<String,String> languageRepository = new HashMap<String,String>();

    private static boolean debug = false;
    private static Level VALUE = Level.getLevel("VALUE");
    private static Level COMMENT = Level.getLevel("COMMENT");
    private static Level LOOP = Level.getLevel("LOOP");
    private static Level debugVerbosity = Level.getLevel("VALUE");
    private static final Logger dev = LogManager.getLogger(PropertyHelper.class.getName());


    /**
     * Get PropertyHelper core.debug value and if true, set debug level (VALUE / COMMENT / LOOP)
     */
    private static void initLogger(){
        debug = StringTool.match(PropertyHelper.config("core.debug"), "^([Tt]rue|[Yy]es|1)$");
        // Here can be change debug verbosity (... < INFO < DEBUG < VALUE < COMMENT < LOOP < TRACE) - no debug = WARN
        if (debug) Configurator.setLevel(dev.getName(), debugVerbosity);
    }


    /**
     * Load a default config to prevent lack of parameters
     */
    public static Map<String,String> getDefault() {
        // ConfigMode.STRICT is a config method locked with enums, ConfigMode.CUSTOM make parameters free and independent
        languageRepository.put("config.mode", ConfigMode.CUSTOM.toString());   // hard parameter (no change with file)
        // core parameters
        languageRepository.put("core.debug", "0");
        languageRepository.put("core.language", "default");
        // colors
        languageRepository.put("", "");

        return languageRepository;
    }


    /**
     * Display list of properties from HashMap
     */
    public static void logDefault(){
        initLogger();
        Map<String, String> sortedDefault = new TreeMap<>(languageRepository);
        for (Map.Entry<String, String> entry : sortedDefault.entrySet()) {
            dev.log(LOOP, entry.getKey() + " : " + entry.getValue());
        }
    }


}
