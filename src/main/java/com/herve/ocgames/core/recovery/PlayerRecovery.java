package com.herve.ocgames.core.recovery;

import com.herve.ocgames.core.Player;

public class PlayerRecovery extends Player {

    /*
     * Constructor
     */

    public PlayerRecovery() {
        super();
        this.codeGenerator = new CodeGeneratorRecovery();
        this.codeChecker = new CodeCheckerRecovery();
        //this.extendedBeginMessage = "extendedMessage";
    }

    public PlayerRecovery(String testWithoutRandom) {
        super();
        this.codeGenerator = new CodeGeneratorRecovery(testWithoutRandom);
        this.codeChecker = new CodeCheckerRecovery();
    }

}
