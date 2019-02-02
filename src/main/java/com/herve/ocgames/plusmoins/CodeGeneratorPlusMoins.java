package com.herve.ocgames.plusmoins;

import com.herve.ocgames.core.CodeGenerator;
import com.herve.ocgames.core.GameCache;
import com.herve.ocgames.utils.Response;
import com.herve.ocgames.utils.StringTool;

public class CodeGeneratorPlusMoins extends CodeGenerator {


    public CodeGeneratorPlusMoins(){
        super();
        debugVerbosity = 2 ;
    }

    public CodeGeneratorPlusMoins(String code) {
        super(code);
        debugVerbosity = 2 ;
    }

    @Override
    public Response generateRandom() {
        if (GameCache.isFailed()) return null;
        String randNum = "";

        debugV3("generate random digit between 0 and 10, (codeLength - 1) times");
        for (int i = 0; i < this.codeLength; i++) {
            // Ajout d'un chiffre aléatoire au code
            randNum += "" + ((int)(Math.random() * 10));
        }
        debugV2(lang("debug.generateRandom") + randNum);

        debugV3("store randnum in GameCache and return null because we don't need response");
        debugV1(lang("debug.generateRandom") + randNum);
        GameCache.setComputerCode(randNum);
        return null;
    }

    @Override
    public Response generateComputerAttempt() {
        if (GameCache.isFailed()) return null;
        // initialize local variables
        Response response = new Response();
        String newAttempt = "";

        debugV3("cross concatenate next computer attempt and next player evaluation (ex : 4444 and ++++ give 4+4+4+4+)");
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
        debugV2("debug.plusmoinsConcatAttemptEvaluation" + newAttempt);

        if (! StringTool.match(newAttempt, "^4{" + this.codeLength + "}$")){
            debugV3("it's not the first attempt (new attempt != 44... , so we use this substitution table");
            String[][] substitutionTable = {{"4\\+", "7"}, {"4-", "2"}, {"7\\+", "8"}, {"7-", "5"},
                    {"2\\+","3"}, {"2-","1"}, {"1-","0"}, {"5\\+","6"}, {"8\\+","9"},{"=", ""}};
            debugV3("we use substitionTable with StringTool arrayReplace to compose the new attempt");
            newAttempt = StringTool.arrayReplace(newAttempt, substitutionTable);
        }
        debugV2("debug.plusmoinsGenerateComputerAttempt" + newAttempt);

        debugV3("we store attempt in GameCache, and return message in response");
        String[][] attemptSubstitutes = new String[][] {{"VAR_COMPUTER_ATTEMPT", newAttempt}};
        GameCache.addComputerAttempt(newAttempt);
        response.setMessage("%n" + lang("game.computerAttempt", attemptSubstitutes));
        return response;

    }

}
