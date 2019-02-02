package com.herve.ocgames.core.interfaces;

public interface PlayerInterface {

    /*
     * All mandatory methods for player are actions (sometimes passives) from player
     */
    
    void letComputerGenerateSecretCode();

    void submitSecretCode();

    void submitAttempt();

    void getComputerEvaluation();

    void getComputerAttempt();

    void submitEvaluation();

}
