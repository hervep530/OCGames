package com.herve.ocgames.mastermind;

import com.herve.ocgames.core.Player;

public class PlayerMasterMind extends Player {

    /*
     *  Constructor
     */

    public PlayerMasterMind() {
        super();
        this.codeGenerator = new CodeGeneratorMasterMind();
        this.codeChecker = new CodeCheckerMasterMind();
    }

    public PlayerMasterMind(String testWithoutRandom) {
        super();
        this.codeGenerator = new CodeGeneratorMasterMind(testWithoutRandom);
        this.codeChecker = new CodeCheckerMasterMind();
    }

}
