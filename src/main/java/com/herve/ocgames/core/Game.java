package com.herve.ocgames.core;

import com.herve.ocgames.core.enums.ConfigMode;
import com.herve.ocgames.core.enums.GameFromList;
import com.herve.ocgames.core.enums.GameVersion;
import com.herve.ocgames.core.factories.PlayerFactory;
import com.herve.ocgames.core.interfaces.GameInterface;
import com.herve.ocgames.utils.StringTool;
import com.herve.ocgames.utils.Text;
import com.herve.ocgames.utils.UserInteraction;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import static com.herve.ocgames.Main.consoleLogger;

public class Game implements GameInterface {

    private GameFromList gameFromList;
    private GameVersion version;
    private String mode;
    private boolean defender;
    private boolean challenger;
    private Player player;
    private String [][] langSubstitutes;

    private boolean debug = false;
    private static Level VALUE = Level.getLevel("VALUE");
    private static Level COMMENT = Level.getLevel("COMMENT");
    private static Level LOOP = Level.getLevel("LOOP");
    private static Level debugVerbosity = Level.getLevel("VALUE");
    private static final Logger dev = LogManager.getLogger(Game.class.getName());

    /**
     *     Constructor
     */
    public Game(){
        int gameMode;
        try {
            initLogger();
            // Get game properties
            gameFromList = GameFromList.valueOf("JEU" + GameChoice.getGameId());
            String versionValue = (PropertyHelper.config(gameFromList.getName() + ".version")).toUpperCase();
            version = GameVersion.valueOf(versionValue);
            gameMode = GameChoice.getGameMode();
            switch (gameMode){
                case 1 :
                    mode = lang("select.challenger");
                    defender = false;
                    challenger = true;
                    break;
                case 2 :
                    mode = lang("select.defender");
                    defender = true;
                    challenger = false;
                    break;
                case 3 :
                    mode = lang("select.dual");
                    defender = true;
                    challenger = true;
                    break;
            }
            ConfigMode configMode = ConfigMode.valueOf(PropertyHelper.config("config.mode").toUpperCase());
            PropertyHelper.loadGame("JEU" + GameChoice.getGameId(), configMode);
            // Initialize Player
            player = PlayerFactory.getPlayer(GameChoice.getGameId());
        } catch (NullPointerException e) {
            // we stop all before GameCache.initialize, so GameCache.isFailed() ==  true, and game is stopped
            consoleLogger.fatal(lang("error.game.instanciate"));
            return;
        }
        // Initialize GameCache and set langSubstitutes for dynamic messages from languageRepository
        GameCache.initialize(gameMode);
        this.langSubstitutes = new String[][] {{"VAR_NAME", GameCache.getGameName()},
                {"VAR_MODE", this.mode},
                {"VAR_CODE_LENGTH", PropertyHelper.config("game.codeLength")},
                {"VAR_DIGITS_IN_GAME", PropertyHelper.config("game.digitsInGame")},
                {"VAR_LAST_DIGIT", String.valueOf(Integer.parseInt(PropertyHelper.config("game.digitsInGame")) -1 )},
                {"VAR_DIGIT_MAX_REPEAT", PropertyHelper.config("game.digitMaxRepeat")},
                {"VAR_EQUAL", "="}
        };
    }

    /**
     * Constructor overloaded only for test - provide a way to predict secret code generated by computer
     * @param computerSecretCode
     */
    public Game(String computerSecretCode){
        // Constructor to force computerSecretCode without random - !!!! only for test !!!!
        super();
        GameCache.setComputerCode(computerSecretCode);
    }

    /**
     * Get PropertyHelper core.debug value and if true, set debug level (VALUE / COMMENT / LOOP)
     */
    private void initLogger(){
        this.debug = StringTool.match(PropertyHelper.config("core.debug"), "^([Tt]rue|[Yy]es|1)$");
        // Here can be change debug verbosity (... < INFO < DEBUG < VALUE < COMMENT < LOOP < TRACE) - no debug = WARN
        if (this.debug) Configurator.setLevel(dev.getName(), debugVerbosity);
    }

    /**
     * Shortcut to get PropertyHelper languages values (messages)
     * @param key (message key as String)
     * @return message in choosen language
     */
    private static String lang(String key){
        return PropertyHelper.language(key);
    }

    /**
     * Shortcut to get PropertyHelper languages values (messages)
     * @param key (message key as String)
     * @param arraySubstitutions 2 dimensions array to substitute string array[i][0] by array[i][1]
     * @return message in choosen language
     */
    private static String lang(String key, String[][] arraySubstitutions){
        return PropertyHelper.language(key, arraySubstitutions);
    }

    /**
     * The public method for this class. it organizes game's events with calling player actions
     */
    public void start(){
        if ( GameCache.isFailed() ) return;
        dev.log(COMMENT,"We execute same code for all modes, but instructions are filtered with if");
        this.welcome();
        if (this.challenger) player.letComputerGenerateSecretCode();
        if (this.debug) UserInteraction.displayMessage("%n");
        if (this.defender) player.submitSecretCode();
        //UserInteraction.clearConsole();
        do {
            if (this.challenger) player.submitAttempt();
            if (this.challenger) player.getComputerEvaluation();
            if (this.challenger && this.defender) UserInteraction.displayMessage("%n");
            if (this.defender) player.getComputerAttempt();
            if (this.defender) player.submitEvaluation();
            if (this.challenger && this.defender) UserInteraction.displayMessage("%n");
            GameCache.nextTurn();
        } while (! GameCache.end());
        UserInteraction.displayMessage("%n");
        this.goodbye();
    }

    /**
     * Begin of the game - message, rules,...
     */
    private void welcome(){
        // Display message and rules, when begin the game
        String message =  "%n" + Text.effect(lang("game.start", this.langSubstitutes),"background",
                "bright_black") + "%n";
        message += Text.effect(lang( GameCache.getGameName() + ".rules." + this.mode, this.langSubstitutes),
                PropertyHelper.config("color.rules"));
        UserInteraction.displayMessage(message);
    }

    /**
     * End of the game
     */
    private void goodbye(){
        if ( GameCache.isFailed() ) return;
        // Display message when end the game
        String endMessage;
        if (GameCache.isWinner()) {
            endMessage = Text.effect(lang("game.winner", this.langSubstitutes), PropertyHelper.config("color.winner"));
        } else {
            endMessage = Text.effect(lang("game.looser", this.langSubstitutes), PropertyHelper.config("color.looser"));
        }
        UserInteraction.displayMessage(endMessage);
    }

}
