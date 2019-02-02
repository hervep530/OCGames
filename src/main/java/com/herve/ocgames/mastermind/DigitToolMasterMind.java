package com.herve.ocgames.mastermind;

import com.herve.ocgames.core.GameCache;
import com.herve.ocgames.core.PropertyHelper;
import com.herve.ocgames.utils.StringTool;

import static com.herve.ocgames.Main.devConsoleLogger;
import static com.herve.ocgames.Main.devLogger;

public class DigitToolMasterMind {

    private int codeLength;
    private int digitsInGame;
    private String digitsInput;
    private String digitsFound;
    private String digitsTried;
    private String digitInProcess;
    private String digitsOutOfCode;
    private String digitsBuildCode;
    private int minus;
    private boolean sorting;
    private boolean debug = false;
    private int debugVerbosity = 2 ;

    /*
     * Constructor
     */

    public DigitToolMasterMind(){
        debugV3("Get properties from PropertyHelper in Class variables");
        this.codeLength = Integer.parseInt(PropertyHelper.config("game.codeLength"));
        this.digitsInGame = Integer.parseInt(PropertyHelper.config("game.digitsInGame"));
        debugV2("codeLength = " + this.codeLength + " / digitsInGame = " + this.digitsInGame);
        debugV3("get list of digits from nb of digit in the game and sort it randomly (make computer strategy unprevisible)"); // comment used for degug
        this.digitsInput = getDigitsSuite(this.digitsInGame);
        this.digitsInput = sortInputDigitsRandomly(this.digitsInput);
        debugV2("Digits list sorted randomly : " + this.digitsInput);
        debugV3("initialize all other variables used in order to compute"); // comment used for degug
        this.digitsFound = "";
        this.digitsTried = "";
        this.digitsOutOfCode = "";
        this.digitsBuildCode = "";
        this.minus = 0;
        this.sorting = false;
        this.debug = StringTool.match(PropertyHelper.config("core.debug"), "^([Tt]rue|[Yy]es|1)$");

    }

    /*
     * Getters and setters
     */

    String getDigitsInput(){
        return this.digitsInput;
    }

    String getDigitsFound(){
        return this.digitsFound;
    }

    String getDigitsOutOfCode(){
        return this.digitsOutOfCode;
    }

    String getDigitsTried() {return this.digitsTried;}

    void addDigitTried(String digit) {this.digitsTried += digit;}

    void setDigitInProcess(String digitInProcess){this.digitInProcess = digitInProcess;}

    String getBuildCode() {
        return this.digitsBuildCode;
    }

    int getMinus() {
        return this.minus;
    }

    boolean isSorting() { return this.sorting; }

    /*
     * Externalize Work for CodeGeneratorMasterMind
     */

    private String getDigitsSuite(int nbDigit){
        // this method is used to have constructor lighter as possible - arg is this.digitsInGame
        String digitsOut = "";
        debugV3("given the range [0-" + nbDigit +"], get all digits in a string"); // comment used for degug
        for (int i = 0 ; i < nbDigit; i++){
            digitsOut += "" + i;
        }
        debugV2("getDigitsSuite return : " + digitsOut);
        return digitsOut;
    }


    private String sortInputDigitsRandomly(String digitsIn){
        String digitsOut = "";
        debugV3("given a String containing a list of digit " + digitsIn + ", reorganize it randomly"); // comment used for degug
        for (int i = 0 ; i < this.digitsInGame; i++){
            int digitIndex = (int)(digitsIn.length() * Math.random());
            digitsOut += digitsIn.charAt(digitIndex);
            digitsIn = digitsIn.replaceAll(String.valueOf(digitsIn.charAt(digitIndex)), "");
        }
        debugV2("sortInputDigitsRandomly return : " + digitsOut);
        return digitsOut;
    }

    void updateAfterFindingStrategy() {
        // Nothing to get at the first turn
        if (GameCache.turn() == 0) return;
        // Initialize local variables
        int nbDigitsInAttempt = GameCache.playerEvaluations()[GameCache.turn() - 1].length();

        if ( ! this.sorting ) {
            debugV3("Before we found all digits, so we don't have sorting task"); // comment used for degug
            if (nbDigitsInAttempt > 0) {
                debugV3("We found this new digit tried in previous attempt, so we update digitFinder data for this digit"); // comment used for degug
                for (int i = 0; i < nbDigitsInAttempt; i++) {
                    this.digitsFound += "" + String.valueOf(this.digitsInput.charAt(GameCache.turn() - 1));
                }
            } else {
                debugV3("We don't found this new digit tried in previous attempt, so we add it in digitOutOfCode"); // comment used for degug
                this.digitsOutOfCode += "" + String.valueOf(this.digitsInput.charAt(GameCache.turn() - 1));
            }
        }

        if ( this.sorting ) {
            debugV3("After all digits are found we sort digits"); // comment used for degug
            if (StringTool.match(GameCache.playerEvaluations()[GameCache.turn() - 1],"^x{1,}$")) {
                debugV3("Digit in previous attempt had good place, so we remove digit from digitFound, add it in buildCode"); // comment used for degug
                this.digitsBuildCode += this.digitInProcess;
                this.digitsFound = this.digitsFound.replaceFirst(digitInProcess, "");
                debugV3("and initialize digitTried and minus"); // comment used for degug
                this.digitsTried = "";
                this.minus = GameCache.turn();
            }
        }

        // This code must be placed after the code of conditionnal structure "if (sorting)" - like a trigger for sorting
        // It will be exceute after the last time we perfoming "not sorting" task, so before beginning sort
        if ( ! this.sorting &&
                (this.digitsFound.length() == this.codeLength || GameCache.turn() == this.digitsInGame)){
            debugV3("Compute the minus to compute index of digitsFound in CodeGenerator.generateComputerAttempt"); // comment used for degug
            this.minus = GameCache.turn();
            debugV3("Update outOfCode, initialize buildCode string, and set sorting to true, when we found all good digits"); // comment used for degug
            if (GameCache.turn() < this.digitsInGame){
                for (int i = GameCache.turn(); i < this.digitsInGame; i++ ){
                    this.digitsOutOfCode += "" + this.digitsInput.charAt(i);
                }
            }
            this.digitsBuildCode = "";
            this.sorting = true;
            debugV2("digitsOutOfCode = " + this.digitsOutOfCode + " / digitsBuildCode = " + this.digitsBuildCode);
        }
    }

    /*
     * Utilities to access log in different verbosity level
     */

    // debug when verbosity level is equal to 1 - Should be used to exceptionnaly log debug message in the console
    //private void debugV1(String message){ if (this.debug && this.debugVerbosity > 0) devConsoleLogger.debug(message); }

    private void debugV2(String message){
        // debug when verbosity level is up to 2 - Should be used to log computed value in file
        if (this.debug && this.debugVerbosity > 1) devLogger.debug(message);
    }

    private void debugV3(String message){
        // debug when verbosity level is up to 3 - Should be used to log message as comment in the code
        if (this.debug && this.debugVerbosity > 2) devLogger.debug(message);
    }

    // debug when verbosity level is up to 4 - Should be exceptionnaly used to log computed value in loop
    //private void debugV4(String message){ if (this.debug && this.debugVerbosity > 3) devLogger.debug(message); }


}
