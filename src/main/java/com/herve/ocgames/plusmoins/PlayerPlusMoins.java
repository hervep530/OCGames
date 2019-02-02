package com.herve.ocgames.plusmoins;

import com.herve.ocgames.core.Player;

public class PlayerPlusMoins extends Player {

    /*
     * Constructor
     */

    public PlayerPlusMoins() {
        super();
        this.codeGenerator = new CodeGeneratorPlusMoins();
        this.codeChecker = new CodeCheckerPlusMoins();
        //this.extendedBeginMessage = "extendedMessage";
    }

    public PlayerPlusMoins(String testWithoutRandom) {
        super();
        this.codeGenerator = new CodeGeneratorPlusMoins(testWithoutRandom);
        this.codeChecker = new CodeCheckerPlusMoins();
    }

}
