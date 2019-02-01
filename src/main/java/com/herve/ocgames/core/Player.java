package com.herve.ocgames.core;

import com.herve.ocgames.core.interfaces.PlayerInterface;
import com.herve.ocgames.utils.Response;
import com.herve.ocgames.utils.StringTool;
import com.herve.ocgames.utils.UserInteraction;

import static com.herve.ocgames.Main.devLogger;

public abstract class Player implements PlayerInterface {

    private boolean debug = false;
    private int debugVerbosity = 2 ;

    public CodeGenerator codeGenerator;
    public CodeChecker codeChecker;
    protected String codeWithoutRandom = "";

    public Player(){
        this.debug = StringTool.match(PropertyHelper.config("core.debug"), "^([Tt]rue|[Yy]es|1)$");
    }

    public Player(String testWithoutRandom) {
        this.debug = StringTool.match(PropertyHelper.config("core.debug"), "^([Tt]rue|[Yy]es|1)$");
        this.codeWithoutRandom = testWithoutRandom;
    }


    public void submitSecretCode(){
        if (GameCache.isFailed()) return;

        debugV3("Get player secret code with prompt");
        this.codeGenerator.askPlayerSecretCode();
        UserInteraction.displayMessage("%n");
    }

    public void letComputerGenerateSecretCode(){
        if (GameCache.isFailed()) return;

        debugV3("Get computer secret code");
        if (GameCache.getComputerCode().contentEquals("")) this.codeGenerator.generateRandom();
    }

    public void submitAttempt(){
        if (GameCache.isFailed()) return;
        String color = "reset";

        debugV3("Get player attempt with prompt");
        if (GameCache.isChallenger() && GameCache.isDefender()) color = PropertyHelper.config("color.challenger");
        Response response = this.codeGenerator.askPlayerCodeAttempt();
        if ( ! GameCache.isFailed() ) UserInteraction.displayMessage(response, color);
    }

    public void getComputerEvaluation(){
        if (GameCache.isFailed()) return;
        String color = "reset";
        String playerAttempt = GameCache.playerAttempts()[GameCache.turn()];
        String computerSecretCode = GameCache.getComputerCode();

        debugV3("Get computer evaluation");
        if (GameCache.isChallenger() && GameCache.isDefender()) color = PropertyHelper.config("color.challenger");
        Response response = this.codeChecker.generateEvaluation(playerAttempt, computerSecretCode);
        if (GameCache.isFailed()) return;
        GameCache.addComputerEvaluation(response.getValue("evaluation").toString());
        if (response.isSuccess()) GameCache.wins();
        UserInteraction.displayMessage(response, color);
    }

    public void getComputerAttempt() {
        if (GameCache.isFailed()) return;

        debugV3("Get computer Attempt");
        String color = "reset";
        Response response = this.codeGenerator.generateComputerAttempt();
        if (GameCache.isChallenger() && GameCache.isDefender()) color = PropertyHelper.config("color.defender");
        if ( ! GameCache.isFailed() ) UserInteraction.displayMessage(response, color);
    }

    public void submitEvaluation(){
        if (GameCache.isFailed()) return;
        String color = "reset";
        String computerAttempt = GameCache.computerAttempts()[GameCache.turn()];
        String playerSecretCode = GameCache.getPlayerCode();

        debugV3("Player evaluation with prompt");
        Response response = this.codeChecker.askPlayerEvaluation();
        if (GameCache.isFailed()) return;
        if (! response.isSuccess()) GameCache.looses();
        String playerEvaluation = response.getValue("evaluation").toString();
        GameCache.addPlayerEvaluation(response.getValue("evaluation").toString());

        debugV3("Silent Computer evaluation");
        response = this.codeChecker.generateEvaluation(computerAttempt, playerSecretCode);
        if (GameCache.isFailed()) return;
        String refereeEvaluation = response.getValue("evaluation").toString();

        debugV3("Referee control");
        if (GameCache.isChallenger() && GameCache.isDefender()) color = PropertyHelper.config("color.defender");
        response = this.codeChecker.askRefereeControl(playerEvaluation, refereeEvaluation);
        if (GameCache.isFailed()) return;
        UserInteraction.displayMessage(response, color);
        if (! response.isSuccess()) GameCache.looses();
    }

    // debug when verbosity level is equal to 1 - Should be used to exceptionnaly log debug message in the console
    //private void debugV1(String message){ if (this.debug && this.debugVerbosity > 0) devConsoleLogger.debug(message); }

    // debug when verbosity level is up to 2 - Should be used to log computed value in file
    //private void debugV2(String message){ if (this.debug && this.debugVerbosity > 1) devLogger.debug(message); }

    // debug when verbosity level is up to 3 - Should be used to log message as comment in the code

    /**
     * a way to debug and comment with the same line (debug activated if core.debug = 1 and debugVerbosity > 2)
     * @param message the comment which will tell what we do in developpement log
     */
    private void debugV3(String message){
        if (this.debug && this.debugVerbosity > 2) devLogger.debug(message);
    }

    // debug when verbosity level is up to 4 - Should be exceptionnaly used to log computed value in loop
    //private void debugV4(String message){ if (this.debug && this.debugVerbosity > 3) devLogger.debug(message); }

}
