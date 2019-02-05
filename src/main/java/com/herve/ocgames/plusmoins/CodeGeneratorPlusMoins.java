package com.herve.ocgames.plusmoins;

import com.herve.ocgames.core.CodeGenerator;
import com.herve.ocgames.core.GameCache;
import com.herve.ocgames.utils.Response;
import com.herve.ocgames.utils.StringTool;

import static com.herve.ocgames.Main.devConsoleLogger;

public class CodeGeneratorPlusMoins extends CodeGenerator {


    public CodeGeneratorPlusMoins(){
        super();
        debugVerbosity = VALUE;
        initLogger();
    }

    public CodeGeneratorPlusMoins(String code) {
        super(code);
        debugVerbosity = VALUE;
        initLogger();
    }

    @Override
    public Response generateRandom() {
        if (GameCache.isFailed()) return null;
        String randNum = "";

        dev.log(COMMENT,"generate random digit between 0 and 10, (codeLength - 1) times");
        for (int i = 0; i < this.codeLength; i++) {
            // Ajout d'un chiffre aléatoire au code
            randNum += "" + ((int)(Math.random() * 10));
        }
        dev.log(VALUE,lang("debug.generateRandom") + randNum);

        dev.log(COMMENT,"store randnum in GameCache and return null because we don't need response");
        devConsoleLogger.debug(lang("debug.generateRandom") + randNum);
        GameCache.setComputerCode(randNum);
        return null;
    }

    @Override
    public Response generateComputerAttempt() {
        if (GameCache.isFailed()) return null;
        // initialize local variables
        Response response = new Response();
        String newAttempt = "";

        dev.log(COMMENT,"cross concatenate next computer attempt and next player evaluation (ex : 4444 and ++++ give 4+4+4+4+)");
        for (int i = 0; i < this.codeLength; i++) {
            if (GameCache.turn() == 0) {
                // particular situation with first attempt in the game
                newAttempt += "4";
            } else {
                // for the next attempts
                newAttempt += GameCache.computerAttempts()[GameCache.turn() - 1].charAt(i) + ""
                        + GameCache.playerEvaluations()[GameCache.turn() - 1].charAt(i);
            }
        }
        dev.log(VALUE,"debug.plusmoinsConcatAttemptEvaluation" + newAttempt);

        if (! StringTool.match(newAttempt, "^4{" + this.codeLength + "}$")){
            dev.log(COMMENT,"it's not the first attempt (new attempt != 44... , so we use this substitution table");
            String[][] substitutionTable = {{"4\\+", "7"}, {"4-", "2"}, {"7\\+", "8"}, {"7-", "5"},
                    {"2\\+","3"}, {"2-","1"}, {"1-","0"}, {"5\\+","6"}, {"8\\+","9"},{"=", ""}};
            dev.log(COMMENT,"we use substitionTable with StringTool arrayReplace to compose the new attempt");
            newAttempt = StringTool.arrayReplace(newAttempt, substitutionTable);
        }
        dev.log(VALUE,"debug.plusmoinsGenerateComputerAttempt" + newAttempt);

        dev.log(COMMENT,"we store attempt in GameCache, and return message in response");
        String[][] attemptSubstitutes = new String[][] {{"VAR_COMPUTER_ATTEMPT", newAttempt}};
        GameCache.addComputerAttempt(newAttempt);
        response.setMessage("%n" + lang("game.computerAttempt", attemptSubstitutes));
        return response;

    }

}
