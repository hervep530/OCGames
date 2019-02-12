package com.herve.ocgames.core.interfaces;

public interface PlayerInterface {

    /*
     * All mandatory methods for player are actions (sometimes passives) from player
     */

    /**
     * Passive action from player - as if he asks computer to generate its secret code
     */
    void letComputerGenerateSecretCode();

    /**
     * Action from player - he choose a secret code and submit it for referee control at each evaluation from player
     */
    void submitSecretCode();

    /**
     * action from player - he give a new attempt to guess secret code and submit it for evaluation from computer
     */
    void submitAttempt();

    /**
     * Passive action from player - as if he asks computer to give answer from his new attempt
     */
    void getComputerEvaluation();

    /**
     * Passive action from player - as if he asks computer to give a new attempt
     */
    void getComputerAttempt();

    /**
     * Action from player - he gives answer to computer after computer attempt
     */
    void submitEvaluation();

}
