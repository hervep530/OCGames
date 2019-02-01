package com.herve.ocgames.core;

public abstract class Player {
    public abstract void letComputerGenerateSecretCode();

    public abstract void submitSecretCode();

    public abstract void submitAttempt();

    public abstract void getComputerEvaluation();

    public abstract void getComputerAttempt();

    public abstract void submitEvaluation();
}
