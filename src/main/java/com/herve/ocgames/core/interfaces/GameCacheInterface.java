package com.herve.ocgames.core.interfaces;

import java.util.Map;

public interface GameCacheInterface {


    /**
     *
     */
    void initialize();

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
     * A getter doesn't follow traditional name's rule
     */
    Map<String,String> strategies();

    /**
     * This kind of setter doesn't follow traditional name's rule
     */
    void addComputerAttempt(String attempt);

    /**
     * A getter doesn't follow traditional name's rule
     */
    String[] computerAttempts();

    /**
     * This kind of setter doesn't follow traditional name's rule
     */
    void addPlayerAttempt(String attempt);

    /**
     * A getter doesn't follow traditional name's rule
     */
    String[] playerAttempts();

    /**
     * This kind of setter doesn't follow traditional name's rule
     */
    void addComputerEvaluation(String evaluation);

    /**
     * A getter doesn't follow traditional name's rule
     */
    String[] computerEvaluations();

    /**
     * This kind of setter doesn't follow traditional name's rule
     */
    void addPlayerEvaluation(String evaluation);

    /**
     * A getter doesn't follow traditional name's rule
     */
    String[] playerEvaluations();


}
