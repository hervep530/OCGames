package com.herve.ocgames.core;

import com.herve.ocgames.core.interfaces.CodeGeneratorInterface;
import com.herve.ocgames.utils.Response;
import com.herve.ocgames.utils.StringTool;
import com.herve.ocgames.utils.UserInteraction;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import static com.herve.ocgames.Main.*;

public abstract class CodeGenerator implements CodeGeneratorInterface {

    protected int codeLength;
    protected int digitsInGame;
    protected int digitMaxRepeat;
    protected String[][] argumentSubstitutes;

    protected boolean debug = false;
    protected static final Level VALUE = Level.getLevel("VALUE");
    protected static final Level COMMENT = Level.getLevel("COMMENT");
    protected static final Level LOOP = Level.getLevel("LOOP");
    protected static Level debugVerbosity = Level.getLevel("VALUE");
    protected static final String loggerName = CodeGenerator.class.getName();
    protected static final Logger dev = LogManager.getLogger(CodeGenerator.class.getName());


    /*
     * Constructor
     */

    public CodeGenerator() {
        codeLength = Integer.parseInt(PropertyHelper.config("game.codeLength"));
        digitsInGame = Integer.parseInt(PropertyHelper.config("game.digitsInGame"));
        digitMaxRepeat = Integer.parseInt(PropertyHelper.config("game.digitMaxRepeat"));
    }

    public CodeGenerator(String code) {
        super();
        GameCache.setComputerCode(code);
    }


    /*
     * Shortcut, utilities to access PropertyHelper language keys
     */

    /**
     * Get PropertyHelper core.debug value and if true, set debug level (VALUE / COMMENT / LOOP)
     */
    protected void initLogger(){
        this.debug = StringTool.match(PropertyHelper.config("core.debug"), "^([Tt]rue|[Yy]es|1)$");
        // Here can be change debug verbosity (... < INFO < DEBUG < VALUE < COMMENT < LOOP < TRACE) - no debug = WARN
        if (this.debug) Configurator.setLevel(loggerName, debugVerbosity);
    }

    protected String lang(String key){
        return PropertyHelper.language(key);
    }

    protected String lang(String key, String[][] arraySubstitutions){
        return PropertyHelper.language(key, arraySubstitutions);
    }

    /**
     * Utility to display a king of history in dual mode (not really easy to use...)
     * @param nbAttempts nb of turn display in history
     * @return a string which give history on one line
     */
    protected String displayResultFromPreviousAttempts(int nbAttempts) {
        String message = "";
        String previousAttempts= "";
        String[][] previousReplace;
        if (GameCache.turn() < 1) return message;

        for (int i = nbAttempts; i > 0; i--){
            if ( GameCache.turn() >= i ) {
                previousAttempts += "" + GameCache.playerAttempts()[GameCache.turn() - i];
                previousAttempts += " / " + GameCache.computerEvaluations()[GameCache.turn() - i] + " - ";
            }
        }
        previousAttempts = previousAttempts.replaceAll(" - $", "");
        previousReplace = new String[][]{{"VAR_PREVIOUS_ATTEMPTS", previousAttempts}};
        message = lang("game.displayPreviousAttempts", previousReplace);
        return message;
    }

    /*
     * Partie Metier
     */

    @Override
    public Response generateRandom() {
        return null;
    }

    @Override
    public Response askPlayerSecretCode() {
        if (GameCache.isFailed()) return null;
        // initialize local variables
        String[][] questionSubstitutes = new String[][] {{"VAR_CODE_LENGTH", Integer.toString(this.codeLength)}};
        String question = lang("game.questionSecretCode", questionSubstitutes);
        String codePattern = "[0-" + (this.digitsInGame -1) + "]{" + this.codeLength + "}";
        Integer[] digitParameters = new Integer[]{this.digitsInGame,0,this.digitMaxRepeat};

        dev.log(COMMENT,"Get player input with UserInteraction scanner");
        String code = "";
        code = UserInteraction.promptInput(question, codePattern, "reset", "digitMaxRepeat",
                digitParameters, false);
        dev.log(COMMENT,lang("debug.logPlayerCode"));

        dev.log(COMMENT,"store the player code in GameCache and return null because we don't need response");
        GameCache.setPlayerCode(code);
        return null;
    }

    @Override
    public Response generateComputerAttempt() {
        return null;
    }

    @Override
    public Response askPlayerCodeAttempt() {
        if (GameCache.isFailed()) return null;
        // initialize local variables
        String color = "reset";
        if (GameCache.isDefender() && GameCache.isChallenger()) color = PropertyHelper.config("color.challenger");
        Response response = new Response();
        String first = "";
        String[][] questionSubstitutes =  new String[][] {{"VAR_CODE_LENGTH", Integer.toString(this.codeLength)}};
        Integer[] digitParameters = new Integer[]{this.digitsInGame,0,this.digitMaxRepeat};

        dev.log(COMMENT,"Build prompt");
        String question = this.displayResultFromPreviousAttempts(5);
        if (GameCache.turn() == 0) first = "First";
        question += lang("game.question" + first + "Attempt", questionSubstitutes);

        dev.log(COMMENT,"Get player input with UserInteraction scanner");
        String codePattern = "[0-9]{" + this.codeLength + "}";
        String code = "";
        code = UserInteraction.promptInput(question, codePattern, color, "digitMaxRepeat",
                digitParameters, false);

        dev.log(COMMENT,"and store the code in GameCache");
        GameCache.addPlayerAttempt(code);
        response.setMessage(lang("game.codeAttemptGiven"));
        return response;
    }

    /*
     * Utilities to access log in different verbosity level
     */

    protected void invalidArgument(String method, String argument){
        this.argumentSubstitutes = new String[][] {{"VAR_METHOD", method}, {"VAR_ARGUMENT", argument}};
        consoleLogger.fatal(lang("error.invalidArgument", this.argumentSubstitutes));
        supportLogger.fatal(lang("support.invalidArgument", this.argumentSubstitutes));
        GameCache.failure();
    }

}
