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
     * @return default language as Map
     */
    public static Map<String,String> getDefault() {
        languageRepository.put("application.quit", "Application is stopped.%n");
        languageRepository.put("select.title", "Choice VAR_CATEGORY%n");
        languageRepository.put("select.question", "What VAR_CATEGORY do you want to choose?%n");
        languageRepository.put("select.game", "Play ");
        languageRepository.put("select.challenger", "challenger");
        languageRepository.put("select.defender", "defender");
        languageRepository.put("select.dual", "dual");
        languageRepository.put("select.replay", "Replay");
        languageRepository.put("select.otherGame", "Choose another game");
        languageRepository.put("select.quit", "Quit");
        languageRepository.put("select.applicationQuit", "Quit application");
        languageRepository.put("select.invalidCategoryVowel", "You don't choose any VAR_CATEGORY from available choices%n");
        languageRepository.put("select.invalidCategoryConsonant", "You don't choose any VAR_CATEGORY from available choices%n");
        languageRepository.put("select.rightAnswer", "As VAR_CATEGORY, you choose : VAR_ANSWER%n");
        languageRepository.put("category.game", "game");
        languageRepository.put("category.mode", "mode");
        languageRepository.put("category.option", "option");
        languageRepository.put("input.question", "Please, enter VAR_INPUT%n");
        languageRepository.put("input.rightAnswer", "Your answer is VAR_RESPONSE.%n");
        languageRepository.put("input.wrongAnswer", "This answer is not valid. Please, try again.%n");
        languageRepository.put("app.welcome", "  oooo   oooo   oooo   oooo  o    o ooooo  oooo%n o    o o      o      o    o oo  oo o     o    %n o    o o      o   oo oooooo o oo o ooo    ooo %n o    o o      o    o o    o o    o o         o%n  oooo   oooo   oooo  o    o o    o ooooo oooo %n%n");
        languageRepository.put("input.validAnswer", "VAR_RESPONSE");
        languageRepository.put("input.invalidAnswer", "This answer is not valid. Please, try again.%n");
        languageRepository.put("game.start", "Game VAR_NAME in mode VAR_MODE%n");
        languageRepository.put("game.playerFindCode", "You found computer secret code.%n");
        languageRepository.put("game.computerDidntFindCode", "You keep your code secret. The computer couldn't found it.%n");
        languageRepository.put("game.winner", "Congratulation. You wan the game VAR_NAME in mode VAR_MODE.%n%n");
        languageRepository.put("game.computerFoundCode", "The computer found your secret code.%n");
        languageRepository.put("game.refereeDisagree", "Your evaluation is wrong. The referee give the game wan by computer on green carpet.%n");
        languageRepository.put("game.looser", "The computer wan the game VAR_NAME in mode VAR_MODE.%n%n");
        languageRepository.put("game.questionSecretCode", "Before playing your first attempt, please enter your secret code (VAR_CODE_LENGTH digits) : ");
        languageRepository.put("game.playerCodeGiven", "%n");
        languageRepository.put("game.questionFirstAttempt", "Your proposal to guess secret code : ");
        languageRepository.put("game.questionAttempt", "Your proposal to guess secret code : ");
        languageRepository.put("mastermind.askPlayerAttempt", "Your proposal to guess secret code : ");
        languageRepository.put("mastermind.resultComputerEvaluation", " -> Computer answer : VAR_RESULT_EVALUATION - VAR_RIGHT_POSITION at good place, VAR_WRONG_POSITION at wrong place%n");
        languageRepository.put("game.resultComputerEvaluation", " -> Computer answer : VAR_RESULT_EVALUATION%n");
        languageRepository.put("game.computerAttempt", "Computer proposal : VAR_COMPUTER_ATTEMPT");
        languageRepository.put("mastermind.playerEvaluation", " -> Your answer (VAR_PLAYER_SECRET) : ");
        languageRepository.put("plusmoins.playerEvaluation", " -> Your answer (VAR_PLAYER_SECRET) : ");
        languageRepository.put("game.refereeAgree", " - VAR_RIGHT_POSITION at good place, VAR_WRONG_POSITION at wrong place%n");
        languageRepository.put("game.refereeWrongEvaluation", "You made a mistake in your evaluation : VAR_PLAYER_EVALUATION instead of VAR_REFEREE_EVALUATION%nThe referee give the game wan by computer on green carpet%n");
        languageRepository.put("mastermind.rules.defender", "Game rules : Choose a secret code and win the game if the computer can't guess it.%nThis code must exactly contains VAR_CODE_LENGTH digits between 0 and VAR_LAST_DIGIT.%nAt each proposal from computer, indicate it :%n- number of digits at good place with \"x\"%n- number of digits at wrong place with \"o\"%n- if it didn't give any good digit with \"-\".%nBe Careful, the referee will control... And if you make a mistake, you loose.%n%n");
        languageRepository.put("mastermind.rules.challenger", "Game rules : Guess the secret code MasterMind. This code exactly contains VAR_CODE_LENGTH digits between 0 and VAR_LAST_DIGIT.%nAt each of your proposal the computer will indicate you :%n- number of digits at good place with \"x\"%n- number of digits at wrong place with \"o\".%n%n");
        languageRepository.put("mastermind.rules.duel", "Game rules : Step by step, Guess the secret code MasterMind and reverse.%nFirst of all, choose and give your secret... and the game will start...%nThe secret code, and so, your attempts contains excatly VAR_CODE_LENGTH digits.%nComputer and you evaluate proposal with the same way :%n- with \"x\" for a digit with good place%n- with \"o\" for a digit with wrong place%n- with \"-\" when proposal doesn't contains any digit of you secret code%n%n");
        languageRepository.put("plusmoins.rules.defender", "Game rules : Choose a secret code and win the game if the computer can't guess it.%nThis code must exactly contains VAR_CODE_LENGTH digits between 0 and VAR_LAST_DIGIT.%nAt each proposal from computer, indicate it for each digit if it must :%n- decrease the value with \"-\"%n- increase the value with \"+\"%n- keep a good digit with \"VAR_EQUAL\".%nBe Careful, the referee will control... And if you make a mistake, you loose.%n%n");
        languageRepository.put("plusmoins.rules.challenger", "Game rules : Guess the secret code MasterMind. This code exactly contains VAR_CODE_LENGTH digits between 0 and VAR_LAST_DIGIT.%nAt each of your proposal the computer will indicate you for each digit if you must  :%n- decrease the value with a \"-\"%n- increase the value with \"+\".%n- keep a good digit with \"VAR_EQUAL\".%n%n");
        languageRepository.put("plusmoins.rules.dual", "Game rules : Step by step, Guess the secret code MasterMind and reverse.%nFirst of all, choose and give your secret... and the game will start...%nThe secret code, and so, your attempts contains excatly VAR_CODE_LENGTH digits.%nComputer and you evaluate proposal with the same way :%n- \"-\" means that the value should be decrease%n- \"+\" means that the value should be increase%n- \"VAR_EQUAL\" means that the value is correct and should be kept.%n%n");
        languageRepository.put("error.fileNotFound", "File VAR_NAME doesn't exist.");
        languageRepository.put("error.noFileAccess", "File VAR_NAME can not be open.");
        languageRepository.put("error.languageInvalidKey", "The PropertyHelper can't find Language entry (message) with this key.");
        languageRepository.put("error.gameInstanciate", "Class Game - Instanciation new Game() failed" );
        languageRepository.put("error.gameCacheInitialize", "Class GameCache - can't initialize GameCache (check values game.* in PropertyHelper)");
        languageRepository.put("debug.menuReplay", "Entry choosen in menu replay : ");
        languageRepository.put("debug.menuJeu", "Entry choosen in menu jeu : ");
        languageRepository.put("debug.menuMode", "Entry choosen in menu mode : ");
        languageRepository.put("debug.generateRandom", "Random computer secret code is : ");
        languageRepository.put("debug.logPlayerCode", "Player secret code is : ");
        languageRepository.put("debug.mastermindGoodPosition", "Evaluation for digits in right position : ");
        languageRepository.put("debug.mastermindWrongPosition", "Evaluation for digits in wrong position : ");
        languageRepository.put("debug.mastermindDigitTriedInAttempt", "Add digits to test in computer attemps : ");
        languageRepository.put("debug.mastermindNewDigitToSort", "New digit to test for the position to guess : ");
        languageRepository.put("debug.mastermindDigitToProcessAddedToBuildCode", "After concatenate digits found and new digits to test, current generated attempt is : ");
        languageRepository.put("debug.mastermindNewAttemptCompletion", "After completing attempt code is : ");
        languageRepository.put("debug.plusmoinsComputerEvaluation", "Compare Player attempts and computer evaluation : ");
        languageRepository.put("debug.plusmoinsConcatAttemptEvaluation", "Prepare new attempt before substitution : ");
        languageRepository.put("debug.plusmoinsGenerateComputerAttempt", "New attempt after, it's ready : ");

        return languageRepository;
    }

    /**
     * Log list of properties from HashMap
     */
    public static void logDefault(){
        initLogger();
        Map<String, String> sortedDefault = new TreeMap<>(languageRepository);
        for (Map.Entry<String, String> entry : sortedDefault.entrySet()) {
            dev.log(LOOP, entry.getKey() + " : " + entry.getValue());
        }
    }

}
