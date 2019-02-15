package com.herve.ocgames.mastermind;

import com.herve.ocgames.core.GameCache;
import com.herve.ocgames.core.PropertyHelper;
import com.herve.ocgames.utils.StringTool;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

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
    private static final Level VALUE = Level.getLevel("VALUE");
    private static final Level COMMENT = Level.getLevel("COMMENT");
    private static final Level LOOP = Level.getLevel("LOOP");
    private static Level debugVerbosity = Level.getLevel("VALUE");
    private static final Logger dev = LogManager.getLogger(DigitToolMasterMind.class.getName());

    /*
     * Constructor
     */

    public DigitToolMasterMind(){
        initLogger();
        dev.log(COMMENT,"Get properties from PropertyHelper in Class variables");
        this.codeLength = Integer.parseInt(PropertyHelper.config("game.codeLength"));
        this.digitsInGame = Integer.parseInt(PropertyHelper.config("game.digitsInGame"));
        dev.log(VALUE,"codeLength = " + this.codeLength + " / digitsInGame = " + this.digitsInGame);
        dev.log(COMMENT,"get list of digits from nb of digit in the game and sort it randomly (make computer strategy unprevisible)"); // comment used for degug
        this.digitsInput = getDigitsSuite(this.digitsInGame);
        this.digitsInput = sortInputDigitsRandomly(this.digitsInput);
        dev.log(VALUE,"Digits list sorted randomly : " + this.digitsInput);
        dev.log(COMMENT,"initialize all other variables used in order to compute"); // comment used for degug
        this.digitsFound = "";
        this.digitsTried = "";
        this.digitsOutOfCode = "";
        this.digitsBuildCode = "";
        this.minus = 0;
        this.sorting = false;
        this.debug = StringTool.match(PropertyHelper.config("core.debug"), "^([Tt]rue|[Yy]es|1)$");

    }

    /**
     * Get PropertyHelper core.debug value and if true, set debug level (VALUE / COMMENT / LOOP)
     */
    private void initLogger(){
        this.debug = StringTool.match(PropertyHelper.config("core.debug"), "^([Tt]rue|[Yy]es|1)$");
        // Here can be change debug verbosity (... < INFO < DEBUG < VALUE < COMMENT < LOOP < TRACE) - no debug = WARN
        if (this.debug) Configurator.setLevel(dev.getName(), debugVerbosity);
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

    /**
     * Given the number of digit of the list return a list of digits as String
     * @param nbDigit number of digits of the list
     * @return string with concatenating all digits
     */
    private String getDigitsSuite(int nbDigit){
        // this method is used to have constructor lighter as possible - arg is this.digitsInGame
        String digitsOut = "";
        dev.log(COMMENT,"given the range [0-" + (nbDigit - 1) +"], get all digits in a string"); // comment used for degug
        for (int i = 0 ; i < nbDigit; i++){
            digitsOut += "" + i;
        }
        dev.log(VALUE,"getDigitsSuite return : " + digitsOut);
        return digitsOut;
    }

    /**
     * Given a string with digits, reorganize it randomly
     * @param digitsIn String which enumerate list of digit
     * @return a new string less predictible
     */
    private String sortInputDigitsRandomly(String digitsIn){
        String digitsOut = "";
        dev.log(COMMENT,"given a String containing a list of digit " + digitsIn + ", reorganize it randomly"); // comment used for degug
        for (int i = 0 ; i < this.digitsInGame; i++){
            int digitIndex = (int)(digitsIn.length() * Math.random());
            digitsOut += digitsIn.charAt(digitIndex);
            digitsIn = digitsIn.replaceAll(String.valueOf(digitsIn.charAt(digitIndex)), "");
        }
        dev.log(VALUE,"sortInputDigitsRandomly return : " + digitsOut);
        return digitsOut;
    }

    /**
     * it 's a part of CodeGeneratorMasterMind (method generateComputerAttempt) externalized to improve lisibility of
     * the code. Compute and store some data from the previous "attempt + evalution"
     * Like this, we identify digits present in the code, digits with right / wrong place, digits not yet tried
     */
    void updateAfterFindingStrategy() {
        // Nothing to get at the first turn
        if (GameCache.turn() == 0) return;
        // Initialize local variables
        int nbDigitsInAttempt = GameCache.playerEvaluations()[GameCache.turn() - 1].length();

        if ( ! this.sorting ) {
            dev.log(COMMENT,"Before we found all digits, so we don't have sorting task"); // comment used for degug
            if (nbDigitsInAttempt > 0) {
                dev.log(COMMENT,"We found this new digit tried in previous attempt, so we update digitFinder data for this digit"); // comment used for degug
                for (int i = 0; i < nbDigitsInAttempt; i++) {
                    this.digitsFound += "" + String.valueOf(this.digitsInput.charAt(GameCache.turn() - 1));
                }
            } else {
                dev.log(COMMENT,"We don't found this new digit tried in previous attempt, so we add it in digitOutOfCode"); // comment used for degug
                this.digitsOutOfCode += "" + String.valueOf(this.digitsInput.charAt(GameCache.turn() - 1));
            }
        }

        if ( this.sorting ) {
            dev.log(COMMENT,"After all digits are found we sort digits"); // comment used for degug
            if (StringTool.match(GameCache.playerEvaluations()[GameCache.turn() - 1],"^x{1,}$")) {
                dev.log(COMMENT,"Digit in previous attempt had good place, so we remove digit from digitFound, add it in buildCode"); // comment used for degug
                this.digitsBuildCode += this.digitInProcess;
                this.digitsFound = this.digitsFound.replaceFirst(digitInProcess, "");
                dev.log(COMMENT,"and initialize digitTried and minus"); // comment used for degug
                this.digitsTried = "";
                this.minus = GameCache.turn();
            }
        }

        // This code must be placed after the code of conditionnal structure "if (sorting)" - like a trigger for sorting
        // It will be exceute after the last time we perfoming "not sorting" task, so before beginning sort
        if ( ! this.sorting &&
                (this.digitsFound.length() == this.codeLength || GameCache.turn() == this.digitsInGame)){
            dev.log(COMMENT,"Compute the minus to compute index of digitsFound in CodeGenerator.generateComputerAttempt"); // comment used for degug
            this.minus = GameCache.turn();
            dev.log(COMMENT,"Update outOfCode, initialize buildCode string, and set sorting to true, when we found all good digits"); // comment used for degug
            if (GameCache.turn() < this.digitsInGame){
                for (int i = GameCache.turn(); i < this.digitsInGame; i++ ){
                    this.digitsOutOfCode += "" + this.digitsInput.charAt(i);
                }
            }
            this.digitsBuildCode = "";
            this.sorting = true;
            dev.log(VALUE,"digitsOutOfCode = " + this.digitsOutOfCode + " / digitsBuildCode = " + this.digitsBuildCode);
        }
    }

}
