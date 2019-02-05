package com.herve.ocgames.core;

import com.herve.ocgames.core.interfaces.CodeCheckerInterface;
import com.herve.ocgames.utils.Response;
import com.herve.ocgames.utils.StringTool;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import static com.herve.ocgames.Main.*;

public abstract class CodeChecker implements CodeCheckerInterface {

    protected int codeLength;
    protected int digitsInGame;
    protected int digitMaxRepeat;
    protected String [][] argumentSubstitutes;

    protected boolean debug = false;
    protected static final Level VALUE = Level.getLevel("VALUE");
    protected static final Level COMMENT = Level.getLevel("COMMENT");
    protected static final Level LOOP = Level.getLevel("LOOP");
    protected static Level debugVerbosity = Level.getLevel("VALUE");
    protected static final String loggerName = CodeChecker.class.getName();
    protected static final Logger dev = LogManager.getLogger(CodeChecker.class.getName());

    /*
     * Constructor
     */

    public CodeChecker() {
        this.codeLength = Integer.parseInt(PropertyHelper.config("game.codeLength"));
        this.digitsInGame = Integer.parseInt(PropertyHelper.config("game.digitsInGame"));
        this.digitMaxRepeat = Integer.parseInt(PropertyHelper.config("game.digitMaxRepeat"));
    }

    /*
     * Utilities
     */

    /**
     * Get PropertyHelper core.debug value and if true, set debug level (VALUE / COMMENT / LOOP)
     */
    protected void initLogger(){
        this.debug = StringTool.match(PropertyHelper.config("core.debug"), "^([Tt]rue|[Yy]es|1)$");
        // Here can be change debug verbosity (... < INFO < DEBUG < VALUE < COMMENT < LOOP < TRACE) - no debug = WARN
        if (this.debug) Configurator.setLevel(loggerName, debugVerbosity);
    }

    /**
     * Shortcut to get PropertyHelper language entry given the key
     * @param key string with form like game.welcome or select.question,...
     * @return the message (value of language entry)
     */
    protected String lang(String key){
        return PropertyHelper.language(key);
    }

    /**
     * Shortcut to get PropertyHelper language entry overloaded for dynamic message
     * @param key string with form like game.welcome or select.question,...
     * @param arraySubstitutions String[][] {{"VAR_TO_REPLACE", "substitute value"},....} to compose dynamic message
     * @return the message (value of language entry)
     */
    protected String lang(String key, String[][] arraySubstitutions){
        return PropertyHelper.language(key, arraySubstitutions);
    }

    protected void invalidArgument(String method, String argument){
        this.argumentSubstitutes = new String[][] {{"VAR_METHOD", method}, {"VAR_ARGUMENT", argument}};
        consoleLogger.fatal(lang("error.invalidArgument", this.argumentSubstitutes));
        supportLogger.fatal(lang("support.invalidArgument", this.argumentSubstitutes));
        GameCache.failure();
    }

    /*
     * Partie Metier
     */

    @Override
    public Response generateEvaluation(String attempt, String secret) {
        return null;
    }

    @Override
    public Response askPlayerEvaluation() {
        return null;
    }

    @Override
    public Response askRefereeControl(String submittedEvaluation, String refereeEvaluation) {
        return null;
    }

}
