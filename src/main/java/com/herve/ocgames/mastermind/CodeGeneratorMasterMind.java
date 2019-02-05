package com.herve.ocgames.mastermind;

import com.herve.ocgames.core.CodeGenerator;
import com.herve.ocgames.core.GameCache;
import com.herve.ocgames.utils.Response;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import static com.herve.ocgames.Main.devConsoleLogger;

public class CodeGeneratorMasterMind extends CodeGenerator {

    private DigitToolMasterMind digitTool = new DigitToolMasterMind();

    /*
     * Contructor
     */

    CodeGeneratorMasterMind() {
        super();
        debugVerbosity = VALUE;
        initLogger();
    }

    CodeGeneratorMasterMind(String code) {
        super(code);
        debugVerbosity = VALUE;
        initLogger();
    }


    @Override
    public Response generateRandom() {
        if (GameCache.isFailed()) return null;
        // initialize local variables
        String randNum = "";
        int newChip;
        int[] chipsCounter = {0,0,0,0,0,0,0,0,0,0};
        boolean nextChip = false;

        dev.log(COMMENT,"generate random digit between 0 and (digitsInGame - 1), (codeLength - 1) times");
        for (int i = 0; i < this.codeLength; i++) {
            do {
                nextChip = false;
                newChip = ((int) (Math.random() * this.digitsInGame));
                if (chipsCounter[newChip] < this.digitMaxRepeat) {
                    chipsCounter[newChip] ++;
                    nextChip = true;
                }
            } while (! nextChip);
            dev.log(COMMENT,"Add random digit to the code");
            randNum += "" + newChip;
        }
        dev.log(VALUE,lang("debug.generateRandom"));

        dev.log(COMMENT,"store this random number in GameCache and we don't need to use answer");
        devConsoleLogger.debug(lang("debug.generateRandom") + randNum);
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

        dev.log(COMMENT,"Always update digitTool from the last attempt in cache before trying a new attempt");
        this.digitTool.updateAfterFindingStrategy();
        dev.log(COMMENT,"Generate attempt in order to find all digits in secret code or in order to sort digits found in code");
        if ( ! this.digitTool.isSorting() && GameCache.turn() < this.digitsInGame
                && this.digitTool.getDigitsFound().length() < this.codeLength){
            dev.log(COMMENT,"Generate attempt in order to find all digits in secret code");
            String digitToProcess = String.valueOf(this.digitTool.getDigitsInput().charAt(GameCache.turn()));
            for (int i = 0; i < this.codeLength; i++){
                newAttempt += digitToProcess;
            }
            dev.log(VALUE,lang("debug.mastermindDigitTriedInAttempt") + newAttempt);
        } else {
            dev.log(COMMENT,"All digits in secret code are found, and we sort digits to find exact secret code");
            String digitToProcess = "";
            dev.log(COMMENT,"We get a digit not tried for the position digitIndex");
            int digitIndex = GameCache.turn() - this.digitTool.getMinus();
            do {
                digitToProcess = String.valueOf(this.digitTool.getDigitsFound().charAt(digitIndex));
                digitIndex ++;
            } while (this.digitTool.getDigitsTried().contains(digitToProcess));
            dev.log(VALUE,lang("debug.mastermindNewDigitToSort") + digitToProcess);
            dev.log(COMMENT,"Digit in process (trying it for the moment) is added to list digitTried in digitTool");
            this.digitTool.addDigitTried(digitToProcess);
            this.digitTool.setDigitInProcess(digitToProcess);
            dev.log(COMMENT,"Add digit in good positions (buildCode), digitToProcess, and complete with bad digit to compose attempt");
            newAttempt = this.digitTool.getBuildCode() + digitToProcess;
            dev.log(VALUE,lang("debug.mastermindDigitToProcessAddedToBuildCode") + newAttempt);
            if (this.digitTool.getDigitsFound().length() > 2) {
                for (int i = newAttempt.length(); i < this.codeLength; i++) {
                    newAttempt += String.valueOf(this.digitTool.getDigitsOutOfCode().charAt(0));
                }
            } else {
                dev.log(COMMENT,"when only 2 digits are in queue to be sorted we add the last one instead of a bad digit");
                newAttempt += this.digitTool.getDigitsFound().replaceFirst(digitToProcess, "");
            }
            dev.log(VALUE,lang("debug.mastermindNewAttemptCompletion") + newAttempt);
        }
        langSubstitutes[0][1] = newAttempt;
        dev.log(COMMENT,"Update GameCache set message in response and return response");
        GameCache.addComputerAttempt(newAttempt);
        response.setMessage("%n" + lang("game.computerAttempt",langSubstitutes));
        return response;
    }


}
