package com.herve.ocgames.core;

import com.herve.ocgames.utils.Response;

public abstract class CodeChecker {
    public abstract Response generateEvaluation(String playerAttempt, String computerSecretCode);

    public abstract Response askPlayerEvaluation();

    public abstract Response askRefereeControl(String playerEvaluation, String refereeEvaluation);
}
