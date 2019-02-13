package com.herve.ocgames.core.interfaces;

import java.util.Map;

public interface GameCacheInterface {

    /*
     * GameCache is a dynamic repository, it will be used like a small database to store volatiles game's data
     */


    /**
     * Initialize Object to prevent Null Pointer errors - replace constructor in static context
     */
    void initialize();

    /*
     * Getter and Setters
     */
    String getName();

    int getTurn();
    void goToNextTurn();

    void setPlayerCode(String code);
    String getPlayerCode();

    void setComputerCode(String code);
    String getComputerCode();

    boolean isWinner();
    void setToWinner();

    boolean isLooser();
    void setToLooser();

    /**
     * Out of used in this version
     * A getter doesn't follow traditional name's rule - store different game's plans for computer attempt (not used in this version)
     * @return a map containing a plan to generate computer attempts (key for each possible case and value for the way to play)
     */
    Map<String,String> strategies();

    /**
     * This kind of setter for array which store computer attempts -  doesn't follow traditional name's rule
     * @param attempt this string is computer attempt for the current turn
     */
    void addComputerAttempt(String attempt);

    /**
     * A getter for array which store computer attempts - doesn't follow traditional name's rule
     * @return array of String which contains all computer attempts in the current game
     */
    String[] computerAttempts();

    /**
     * This kind of setter for array which store player attempts -  doesn't follow traditional name's rule
     * @param attempt this string is player attempt for the current turn
     */
    void addPlayerAttempt(String attempt);

    /**
     * A getter for array which store player attempts - doesn't follow traditional name's rule
     * @return array of String which contains all player attempts in the current game
     */
    String[] playerAttempts();

    /**
     * This kind of setter for array which store computer evaluation -  doesn't follow traditional name's rule
     * @param evaluation  this string is computer evalution for the current turn
     */
    void addComputerEvaluation(String evaluation);

    /**
     * A getter for array which store computer evalution - doesn't follow traditional name's rule
     * @return array of String which contains all computer evaluations in the current game
     */
    String[] computerEvaluations();

    /**
     * This kind of setter for array which store player evaluation -  doesn't follow traditional name's rule
     * @param evaluation this string is player evaluation for the current turn
     */
    void addPlayerEvaluation(String evaluation);

    /**
     * A getter for array which store player evaluation - doesn't follow traditional name's rule
     * @return array of String which contains all player evaluations in the current game
     */
    String[] playerEvaluations();


}
