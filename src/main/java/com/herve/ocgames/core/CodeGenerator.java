package com.herve.ocgames.core;

import com.herve.ocgames.utils.Response;

public abstract class CodeGenerator {
    public abstract void askPlayerSecretCode();

    public abstract void generateRandom();

    public abstract Response askPlayerCodeAttempt();

    public abstract Response generateComputerAttempt();
}
