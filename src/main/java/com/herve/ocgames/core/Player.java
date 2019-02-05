package com.herve.ocgames.core;

import com.herve.ocgames.core.interfaces.PlayerInterface;
import com.herve.ocgames.utils.Response;
import com.herve.ocgames.utils.StringTool;
import com.herve.ocgames.utils.UserInteraction;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public abstract class Player implements PlayerInterface {

    private boolean debug = false;
    private static Level VALUE = Level.getLevel("VALUE");
    private static Level COMMENT = Level.getLevel("COMMENT");
    private static Level LOOP = Level.getLevel("LOOP");
    private static Level debugVerbosity = Level.getLevel("VALUE");
    private static final Logger dev = LogManager.getLogger(Player.class.getName());

    public CodeGenerator codeGenerator;
    public CodeChecker codeChecker;
    protected String codeWithoutRandom = "";

    public Player(){
        initLogger();
    }

    public Player(String testWithoutRandom) {
        initLogger();
        this.codeWithoutRandom = testWithoutRandom;
    }

    /**
     * Get PropertyHelper core.debug value and if true, set debug level (VALUE / COMMENT / LOOP)
     */
    private void initLogger(){
        this.debug = StringTool.match(PropertyHelper.config("core.debug"), "^([Tt]rue|[Yy]es|1)$");
        // Here can be change debug verbosity (... < INFO < DEBUG < VALUE < COMMENT < LOOP < TRACE) - no debug = WARN
        if (this.debug) Configurator.setLevel(dev.getName(), debugVerbosity);
    }

    public void submitSecretCode(){
        if (GameCache.isFailed()) return;

        dev.log(COMMENT,"Get player secret code with prompt");
        this.codeGenerator.askPlayerSecretCode();
        UserInteraction.displayMessage("%n");
    }

    public void letComputerGenerateSecretCode(){
        if (GameCache.isFailed()) return;

        dev.log(COMMENT,"Get computer secret code");
        if (GameCache.getComputerCode().contentEquals("")) this.codeGenerator.generateRandom();
    }

    public void submitAttempt(){
        if (GameCache.isFailed()) return;
        String color = "reset";

        dev.log(COMMENT,"Get player attempt with prompt");
        if (GameCache.isChallenger() && GameCache.isDefender()) color = PropertyHelper.config("color.challenger");
        Response response = this.codeGenerator.askPlayerCodeAttempt();
        if ( ! GameCache.isFailed() ) UserInteraction.displayMessage(response, color);
    }

    public void getComputerEvaluation(){
        if (GameCache.isFailed()) return;
        String color = "reset";
        String playerAttempt = GameCache.playerAttempts()[GameCache.turn()];
        String computerSecretCode = GameCache.getComputerCode();

        dev.log(COMMENT,"Get computer evaluation");
        if (GameCache.isChallenger() && GameCache.isDefender()) color = PropertyHelper.config("color.challenger");
        Response response = this.codeChecker.generateEvaluation(playerAttempt, computerSecretCode);
        if (GameCache.isFailed()) return;
        GameCache.addComputerEvaluation(response.getValue("evaluation").toString());
        if (response.isSuccess()) GameCache.wins();
        UserInteraction.displayMessage(response, color);
    }

    public void getComputerAttempt() {
        if (GameCache.isFailed()) return;

        dev.log(COMMENT,"Get computer Attempt");
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

        dev.log(COMMENT,"Player evaluation with prompt");
        Response response = this.codeChecker.askPlayerEvaluation();
        if (GameCache.isFailed()) return;
        if (! response.isSuccess()) GameCache.looses();
        String playerEvaluation = response.getValue("evaluation").toString();
        GameCache.addPlayerEvaluation(response.getValue("evaluation").toString());

        dev.log(COMMENT,"Silent Computer evaluation");
        response = this.codeChecker.generateEvaluation(computerAttempt, playerSecretCode);
        if (GameCache.isFailed()) return;
        String refereeEvaluation = response.getValue("evaluation").toString();

        dev.log(COMMENT,"Referee control");
        if (GameCache.isChallenger() && GameCache.isDefender()) color = PropertyHelper.config("color.defender");
        response = this.codeChecker.askRefereeControl(playerEvaluation, refereeEvaluation);
        if (GameCache.isFailed()) return;
        UserInteraction.displayMessage(response, color);
        if (! response.isSuccess()) GameCache.looses();
    }

}
