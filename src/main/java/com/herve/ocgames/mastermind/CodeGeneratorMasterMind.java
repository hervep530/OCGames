package com.herve.ocgames.mastermind;

import com.herve.ocgames.core.CodeGenerator;
import com.herve.ocgames.core.GameCache;
import com.herve.ocgames.utils.Response;

public class CodeGeneratorMasterMind extends CodeGenerator {

    private DigitToolMasterMind digitTool = new DigitToolMasterMind();

    /*
     * Contructor
     */

    CodeGeneratorMasterMind() {
        super();
        debugVerbosity = 2 ;
    }

    CodeGeneratorMasterMind(String code) {
        super(code);
        debugVerbosity = 2 ;
    }

    @Override
    public Response generateRandom() {
        if (GameCache.isFailed()) return null;
        // initialize local variables
        String randNum = "";
        int newChip;
        int[] chipsCounter = {0,0,0,0,0,0,0,0,0,0};
        boolean nextChip = false;

        debugV3("generate random digit between 0 and (digitsInGame - 1), (codeLength - 1) times");
        for (int i = 0; i < this.codeLength; i++) {
            do {
                nextChip = false;
                newChip = ((int) (Math.random() * this.digitsInGame));
                if (chipsCounter[newChip] < this.digitMaxRepeat) {
                    chipsCounter[newChip] ++;
                    nextChip = true;
                }
            } while (! nextChip);
            debugV3("Add random digit to the code");
            randNum += "" + newChip;
        }
        debugV2(lang("debug.generateRandom"));

        debugV3("store this random number in GameCache and we don't need to use answer");
        debugV1(lang("debug.generateRandom") + randNum);
        GameCache.setComputerCode(randNum);
        return null;
    }

    @Override
    public Response generateComputerAttempt() {
        if (GameCache.isFailed()) return null;
        // initialize local variables
        int repeat = this.codeLength;
        String newAttempt = "";
        Response response = new Response();
        String[][] langSubstitutes = new String[][] {{"VAR_COMPUTER_ATTEMPT",""}};

        debugV3("Always update digitTool from the last attempt in cache before trying a new attempt");
        this.digitTool.updateAfterFindingStrategy();
        debugV3("Generate attempt in order to find all digits in secret code or in order to sort digits found in code");
        if ( ! this.digitTool.isSorting() && GameCache.turn() < this.digitsInGame
                && this.digitTool.getDigitsFound().length() < this.codeLength){
            debugV3("Generate attempt in order to find all digits in secret code");
            String digitToProcess = String.valueOf(this.digitTool.getDigitsInput().charAt(GameCache.turn()));
            for (int i = 0; i < this.codeLength; i++){
                newAttempt += digitToProcess;
            }
            debugV2(lang("debug.mastermindDigitTriedInAttempt") + newAttempt);
        } else {
            debugV3("All digits in secret code are found, and we sort digits to find exact secret code");
            String digitToProcess = "";
            debugV3("We get a digit not tried for the position digitIndex");
            int digitIndex = GameCache.turn() - this.digitTool.getMinus();
            do {
                digitToProcess = String.valueOf(this.digitTool.getDigitsFound().charAt(digitIndex));
                digitIndex ++;
            } while (this.digitTool.getDigitsTried().contains(digitToProcess));
            debugV2(lang("debug.mastermindNewDigitToSort") + digitToProcess);
            debugV3("Digit in process (trying it for the moment) is added to list digitTried in digitTool");
            this.digitTool.addDigitTried(digitToProcess);
            this.digitTool.setDigitInProcess(digitToProcess);
            debugV3("Add digit in good positions (buildCode), digitToProcess, and complete with bad digit to compose attempt");
            newAttempt = this.digitTool.getBuildCode() + digitToProcess;
            debugV2(lang("debug.mastermindDigitToProcessAddedToBuildCode") + newAttempt);
            if (this.digitTool.getDigitsFound().length() > 2) {
                for (int i = newAttempt.length(); i < this.codeLength; i++) {
                    newAttempt += String.valueOf(this.digitTool.getDigitsOutOfCode().charAt(0));
                }
            } else {
                debugV3("when only 2 digits are in queue to be sorted we add the last one instead of a bad digit");
                newAttempt += this.digitTool.getDigitsFound().replaceFirst(digitToProcess, "");
            }
            debugV2(lang("debug.mastermindNewAttemptCompletion") + newAttempt);
        }
        langSubstitutes[0][1] = newAttempt;
        debugV3("Update GameCache set message in response and return response");
        GameCache.addComputerAttempt(newAttempt);
        response.setMessage("%n" + lang("game.computerAttempt",langSubstitutes));
        return response;
    }


}
