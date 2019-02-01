package com.herve.ocgames.core;

/*
 * GameCache is like as Global variable object attached to a game "session" to store all dynamic data for the game in progress
 * It comes with static getters and setters and make data available from player, code checker or code generator
 */

import com.herve.ocgames.utils.FileTool;
import com.herve.ocgames.utils.StringTool;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.herve.ocgames.Main.*;

public class GameCache {

    // essential for the game
    private static String gameName;
    private static String gameVersion;
    private static int currentTurn ;
    // secret code - player / computer
    private static String playerCode;
    private static String computerCode;
    // the history
    private static String[] computerAttemptsRepository;
    private static String[] playerAttemptsRepository;
    private static String[] computerEvaluationsRepository;
    private static String[] playerEvaluationsRepository;
    private static Map<String,String> strategiesRepository = new TreeMap<>();
    // status which give the issue of the game
    private static boolean challenger = false;
    private static boolean defender = false;
    private static boolean winner;
    private static boolean looser;
    private static boolean fatalError = true;
    private static boolean debug = false;
    private static int debugVerbosity = 2 ;

    /**
     * Do the same work as a constructor
     * @param modeId gameMode id submit directly by appController with selectGame
     */
    public static void initialize(int modeId){
        try {
            challenger = false;
            defender = false;
            winner = false;
            looser = false;
            fatalError = false;
            gameName = PropertyHelper.config("game.name");
            gameVersion = PropertyHelper.config("game.version");
            currentTurn = 0;
            debugV2(gameName + " - " + gameVersion + " - Prochain tour : " + currentTurn);
            playerCode = "";
            computerCode = "";
            // importStrategiesFromFile();  // kept as backup (works but only implemented for one version... and complex)
            debugV2("Nombre de tours à jouer : " + PropertyHelper.config("game.attempts"));
            computerAttemptsRepository = new String[Integer.parseInt(PropertyHelper.config("game.attempts"))];
            playerAttemptsRepository = new String[Integer.parseInt(PropertyHelper.config("game.attempts"))];
            computerEvaluationsRepository = new String[Integer.parseInt(PropertyHelper.config("game.attempts"))];
            playerEvaluationsRepository = new String[Integer.parseInt(PropertyHelper.config("game.attempts"))];
            if (modeId == 1 || modeId ==3) challenger = true;
            if (modeId == 2 || modeId ==3) defender = true;
        } catch (NullPointerException e) {
            consoleLogger.fatal(lang("error.gameCacheInitialize"));
            return;
        }
    }

    /*
     * Getters and setters
     */

    public static String getGameName(){
        return gameName;
    }

    public static String getGameVersion(){
        return gameVersion;
    }

    public static void setPlayerCode(String code){
        playerCode = code;
    }

    public static String getPlayerCode(){
        return playerCode ;
    }

    public static void setComputerCode(String code){
        computerCode = code ;
    }

    public static String getComputerCode(){
        return computerCode ;
    }

    public static Map<String,String> strategies(){
        return strategiesRepository;
    }

    /*
     * Still Getters and setters... but a bit special
     * computer / player attempts and evalutions are stored in array named like this :
     * [computer|player][Attempts|Evaluations]Repository
     * [computer|player][Attempts|Evaluations] is used as a getter
     * add[Computer|Player][Attempt|Evaluation] is used as a setter and only works with appending value
     */
    public static String[] computerAttempts(){
        return  computerAttemptsRepository;
    }

    public static void addComputerAttempt(String attempt){
        computerAttemptsRepository[currentTurn] = attempt;
    }

    public static String[] playerAttempts(){
        return  playerAttemptsRepository;
    }

    public static void addPlayerAttempt(String attempt){
        playerAttemptsRepository[currentTurn] = attempt;
    }

    public static String[] computerEvaluations(){
        return  computerEvaluationsRepository;
    }

    public static void addComputerEvaluation(String evaluation){
        computerEvaluationsRepository[currentTurn] = evaluation;
    }

    public static String[] playerEvaluations(){
        return  playerEvaluationsRepository;
    }

    public static void addPlayerEvaluation(String evaluation){
        playerEvaluationsRepository[currentTurn] = evaluation;
    }

    /* getter and setter for turn, fatalError, winner, looser  */

    public static int turn(){
        return currentTurn;
    }

    public static void nextTurn(){
        currentTurn ++ ;
    }

    public static void failure(){fatalError =true;}

    public static boolean isFailed(){return fatalError;}

    public static boolean isWinner() { return winner; }

    public static boolean isDefender(){return defender;}

    public static boolean isChallenger(){return challenger;}

    public static void wins() { winner = true; }

    public static boolean isLooser() { return looser; }

    public static void looses() { looser = true; }


    /*
     * Shortcut to get propertyHelper language entry
     */

    private static String lang(String key){
        return PropertyHelper.language(key);
    }

    /*
     * Method in order to import strategy file - Not in used but kept as backup
     *
    private static void importStrategiesFromFile(){
        // Game Strategy
        String strategyIdentifier = gameName.replaceAll("^[^0-9]*", "");
        String fileName = "resources/" + gameName + "/" + gameName + "-" + strategyIdentifier + ".properties";
        Map<String,String> tmpStrategiesRepository = new HashMap<>();
        Map<String,String> tmpSubstitutions = new HashMap<>();
        if ( Files.exists(Paths.get(fileName))) {
            System.out.println(gameVersion);
            String filterKey = "^" + gameVersion + "-[0-9]{3,}$";
            String filterValue = "^([a-zA-Z0-9]{4}(:[a-zA-Z0-9]{4}){0,1}|" + gameVersion +
                    "-[0-9]{3}:" + gameVersion +"-[0-9]{3}(,[a-zA-Z0-9]:[a-zA-Z0-9]){6}|[A-Z,]{14})$";
            FileTool.getArrayListFromFile(fileName).stream()
                    .filter(property -> StringTool.match(property[0], filterKey))
                    .filter(property -> StringTool.match(property[1], filterValue))
                    .forEach(property -> tmpStrategiesRepository.put(property[0], property[1]));
        } else {
            supportLogger.warn("GameCache - importStrategiesFromFile failed");
        }
        if ( Files.exists(Paths.get(fileName))) {
            System.out.println(gameVersion);
            String filterKey = "^" + gameVersion + "-[0-9]{3,}replace$";
            String filterValue = "^([a-zA-Z0-9]{4}(:[a-zA-Z0-9]{4}){0,1}|" + gameVersion +
                    "-[0-9]{3}:" + gameVersion +"-[0-9]{3}(,[a-zA-Z0-9]:[a-zA-Z0-9]){6}|[A-Z,]{14})$";
            FileTool.getArrayListFromFile(fileName).stream()
                    .filter(property -> StringTool.match(property[0], filterKey))
                    .filter(property -> StringTool.match(property[1], filterValue))
                    .forEach(property -> tmpSubstitutions.put(property[0], property[1]));
        } else {
            supportLogger.warn("GameCache - importStrategiesFromFile failed");
        }

        for (Map.Entry<String,String> arrayReplaceEntry: tmpSubstitutions.entrySet()){
            String array1[] = arrayReplaceEntry.getValue().split(",");
            String array2[][] = new String[array1.length][2];
            for (int i = 0 ; i < array1.length; i++){
                array2[i][0] = array1[i].split(":")[0];
                array2[i][1] = array1[i].split(":")[1];
            }
            tmpStrategiesRepository.entrySet().stream()
                    .filter(strategy-> StringTool.match(strategy.getKey(),"^" + array2[0][0] + "[0-9]*$"))
                    .forEach(strategy->strategiesRepository.put(
                            strategy.getKey().replaceAll(array2[0][0],array2[0][1]),
                            StringTool.arrayReplace(strategy.getValue(),array2)
                    ));
        }
    }
     */

    /**
     * Test if the game is ended
     * @return boolean end or not
     */
    public static boolean end(){
        boolean endGame = false;
        if (winner || looser || currentTurn > (Integer.parseInt(PropertyHelper.config("game.attempts")) - 1))
            endGame = true;
        return endGame;
    }

    /*
     * Not in used - display computer strategies imported from files for defender mode - only keep as backup
    public static void displayStrategy(){
        // Game Strategy
        for (Map.Entry entry : strategiesRepository.entrySet()){
            System.out.println(entry.getKey() + " : " + entry.getValue());
        };
    }
     */

    // debug when verbosity level is equal to 1 - Should be used to exceptionnaly log debug message in the console
    //private static void debugV1(String message){ if (debug && debugVerbosity > 0) devConsoleLogger.debug(message); }

    private static void debugV2(String message){
        // debug when verbosity level is up to 2 - Should be used to log computed value in file
        if (debug && debugVerbosity > 1) devLogger.debug(message);
    }

    private static void debugV3(String message){
        // debug when verbosity level is up to 3 - Should be used to log message as comment in the code
        if (debug && debugVerbosity > 2) devLogger.debug(message);
    }

    // debug when verbosity level is up to 4 - Should be exceptionnaly used to log computed value in loop
    //private static void debugV4(String message){ if (debug && debugVerbosity > 3) devLogger.debug(message); }

}
