package com.herve.ocgames.core;

import com.herve.ocgames.core.interfaces.CodeCheckerInterface;
import com.herve.ocgames.utils.Response;
import com.herve.ocgames.utils.StringTool;

import static com.herve.ocgames.Main.*;

public abstract class CodeChecker implements CodeCheckerInterface {

    protected int codeLength;
    protected int digitsInGame;
    protected int digitMaxRepeat;
    protected int debugVerbosity;
    protected boolean debug = false;
    protected String [][] argumentSubstitutes;

    /*
     * Constructor
     */

    public CodeChecker() {
        this.debugVerbosity = 2;
        this.codeLength = Integer.parseInt(PropertyHelper.config("game.codeLength"));
        this.digitsInGame = Integer.parseInt(PropertyHelper.config("game.digitsInGame"));
        this.digitMaxRepeat = Integer.parseInt(PropertyHelper.config("game.digitMaxRepeat"));
        this.debug = StringTool.match(PropertyHelper.config("core.debug"), "^([Tt]rue|[Yy]es|1)$");
    }

    /*
     * Utilities
     */

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

    /*
     * Log - shortcut to access log in debug regarding verbosity level (defined for each class - defaut = 2)
     */

    // debug when verbosity level is equal to 1 - Should be used to exceptionnaly log debug message in the console
    //protected void debugV1(String message){ if (this.debug && this.debugVerbosity > 0) devConsoleLogger.debug(message); }

    protected void debugV2(String message){
        // debug when verbosity level is up to 2 - Should be used to log computed value in file
        if (this.debug && this.debugVerbosity > 1) devLogger.debug(message);
    }

    protected void debugV3(String message){
        // debug when verbosity level is up to 3 - Should be used to log message as comment in the code
        if (this.debug && this.debugVerbosity > 2) devLogger.debug(message);
    }

    protected void debugV4(String message){
        // debug when verbosity level is up to 4 - Should be exceptionnaly used to log computed value in loop
        if (this.debug && this.debugVerbosity > 3) devLogger.debug(message);
    }

}
