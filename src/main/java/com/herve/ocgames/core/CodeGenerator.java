package com.herve.ocgames.core;

import com.herve.ocgames.core.interfaces.CodeGeneratorInterface;
import com.herve.ocgames.utils.Response;
import com.herve.ocgames.utils.StringTool;
import com.herve.ocgames.utils.UserInteraction;

import static com.herve.ocgames.Main.*;

public abstract class CodeGenerator implements CodeGeneratorInterface {

    protected int codeLength;
    protected int digitsInGame;
    protected int digitMaxRepeat;
    protected boolean debug = false;
    protected int debugVerbosity;
    protected String[][] argumentSubstitutes;


    public CodeGenerator() {
        codeLength = Integer.parseInt(PropertyHelper.config("game.codeLength"));
        digitsInGame = Integer.parseInt(PropertyHelper.config("game.digitsInGame"));
        digitMaxRepeat = Integer.parseInt(PropertyHelper.config("game.digitMaxRepeat"));
        this.debug = StringTool.match(PropertyHelper.config("core.debug"), "^([Tt]rue|[Yy]es|1)$");
        this.debugVerbosity = 2;
    }

    public CodeGenerator(String code) {
        super();
        GameCache.setComputerCode(code);
    }

    protected String lang(String key){
        return PropertyHelper.language(key);
    }

    protected String lang(String key, String[][] arraySubstitutions){
        return PropertyHelper.language(key, arraySubstitutions);
    }

    protected void invalidArgument(String method, String argument){
        this.argumentSubstitutes = new String[][] {{"VAR_METHOD", method}, {"VAR_ARGUMENT", argument}};
        consoleLogger.fatal(lang("error.invalidArgument", this.argumentSubstitutes));
        supportLogger.fatal(lang("support.invalidArgument", this.argumentSubstitutes));
        GameCache.failure();
    }

    protected void debugV1(String message){
        // debug when verbosity level is equal to 1 - Should be used to exceptionnaly log debug message in the console
        if (this.debug && this.debugVerbosity > 0) devConsoleLogger.debug(message);
    }

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

        debugV3("Get player input with UserInteraction scanner");
        String code = "";
        code = UserInteraction.promptInput(question, codePattern, "reset", "digitMaxRepeat",
                digitParameters, false);
        debugV2(lang("debug.logPlayerCode"));

        debugV3("store the player code in GameCache and return null because we don't need response");
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

        debugV3("Build prompt");
        String question = this.displayResultFromPreviousAttempts(5);
        if (GameCache.turn() == 0) first = "First";
        question += lang("game.question" + first + "Attempt", questionSubstitutes);

        debugV3("Get player input with UserInteraction scanner");
        String codePattern = "[0-9]{" + this.codeLength + "}";
        String code = "";
        code = UserInteraction.promptInput(question, codePattern, color, "digitMaxRepeat",
                digitParameters, false);

        debugV3("and store the code in GameCache");
        GameCache.addPlayerAttempt(code);
        response.setMessage(lang("game.codeAttemptGiven"));
        return response;
    }

}
