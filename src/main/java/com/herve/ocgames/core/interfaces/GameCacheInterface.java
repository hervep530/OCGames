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
     * A getter doesn't follow traditional name's rule - store different game's plans for computer attempt (not used in this version)
     */
    Map<String,String> strategies();

    /**
     * This kind of setter for array which store computer attempts -  doesn't follow traditional name's rule
     */
    void addComputerAttempt(String attempt);

    /**
     * A getter for array which store computer attempts - doesn't follow traditional name's rule
     */
    String[] computerAttempts();

    /**
     * This kind of setter for array which store player attempts -  doesn't follow traditional name's rule
     */
    void addPlayerAttempt(String attempt);

    /**
     * A getter for array which store player attempts - doesn't follow traditional name's rule
     */
    String[] playerAttempts();

    /**
     * This kind of setter for array which store computer evaluation -  doesn't follow traditional name's rule
     */
    void addComputerEvaluation(String evaluation);

    /**
     * A getter for array which store computer evalution - doesn't follow traditional name's rule
     */
    String[] computerEvaluations();

    /**
     * This kind of setter for array which store player evaluation -  doesn't follow traditional name's rule
     */
    void addPlayerEvaluation(String evaluation);

    /**
     * A getter for array which store player evaluation - doesn't follow traditional name's rule
     */
    String[] playerEvaluations();


}
