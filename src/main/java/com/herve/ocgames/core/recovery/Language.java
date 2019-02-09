package com.herve.ocgames.core.recovery;

import com.herve.ocgames.core.PropertyHelper;
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

public class Language {
    private static HashMap<String,String> configRepository = new HashMap<String,String>();
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
    private static void getDefault() {
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
     * Display list of properties from HashMap
     */
    public static void logDefault(){
        initLogger();
        Map<String, String> sortedDefault = new TreeMap<>(configRepository);
        for (Map.Entry<String, String> entry : sortedDefault.entrySet()) {
            dev.log(LOOP, entry.getKey() + " : " + entry.getValue());
        }
    }

}
