package com.herve.ocgames.utils;

import com.herve.ocgames.utils.exceptions.InvalidDigitParametersForMatchingMethod;
import com.herve.ocgames.utils.exceptions.InvalidInputStringForMatchingMethod;
import com.herve.ocgames.utils.exceptions.InvalidPatternForMatchingMethod;
import com.herve.ocgames.utils.exceptions.InvalidRuleNameForMatchingMethod;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTool {

    private static Logger supportLogger = Logger.getLogger("support_file");
    private static Logger devLogger = Logger.getLogger("development_file");

    /**
     * A tool for massive substitutions
     * @param inputString String on which we apply arrayReplace
     * @param arraySubstitutions String array in 2 dimensions which contains several pairs {String to search, substitute}
     * @return String after all substitutions
     */
    public static String arrayReplace(String inputString, String[][] arraySubstitutions) {
        if (arraySubstitutions == null) return inputString;
        String newStringValue = inputString;
        for (String[] substitution : arraySubstitutions) {
            newStringValue = newStringValue.replaceAll(substitution[0], substitution[1]);
        }
        return newStringValue;
    }

    /**
     * A home matcher to avoid to repeat this 3 lines again and again
     * @param inputString String on which we seach pattern
     * @param referencePattern this String will used to build pattern
     * @return true if matches, else false
     */
    public static boolean match(String inputString, String referencePattern) {
        if ( referencePattern == null ){
            devLogger.fatal("You can't use match method with null referencePattern");
            throw new InvalidPatternForMatchingMethod();
        }
        if ( referencePattern.contentEquals("") ){
            devLogger.fatal("You can't use match method with empty referencePattern");
            throw new InvalidPatternForMatchingMethod();
        }
        if (inputString == null) {
            devLogger.fatal("You can't use match method with null inputString");
            throw new InvalidInputStringForMatchingMethod();
        }
        if (inputString.contentEquals("")) return false;
        Pattern p = Pattern.compile(referencePattern);
        Matcher m = p.matcher(inputString);
        return m.matches();
    }

    /**
     * A custom matcher
     * @param inputString String to check
     * @param ruleName rule name
     * @param numericParameters array of integer, to give numerics parameters (usage to define for each rule)
     * @return true when matches, else false
     */
    public static boolean matchSpecificDigitRule (String inputString, String ruleName,Integer[] numericParameters){
        // default value
        boolean isMatching = true;

        if ( ruleName == null ){
            devLogger.fatal("You can't use matchSpecificDigitRule method with null ruleName");
            throw new InvalidRuleNameForMatchingMethod();
        }
        if (inputString == null) {
            devLogger.fatal("You can't use matchSpecificDigitRule method with null inputString");
            throw new InvalidInputStringForMatchingMethod();
        }
        if (! match(inputString, "^[0-9]{1,}$")){
            devLogger.fatal("You can't use matchSpecificDigitRule method if inputString doesn't contain only digits");
            throw new InvalidInputStringForMatchingMethod();
        }

        // switch on all rulename, and default throw exception
        switch (ruleName){
            case "digitMaxRepeat":
                // Rules to control times we repeat a digit in String only composed with digits from 0 to nbDigit - 1
                // numeric parameters - array of 3 values : the first <= 10 and the 2nd <= the third
                int nbDigits;
                int minRepeat;
                int maxRepeat;
                try {
                    nbDigits = numericParameters[0];
                    minRepeat = numericParameters[1];
                    maxRepeat = numericParameters[2];
                } catch (ArrayIndexOutOfBoundsException e){
                    devLogger.fatal("Array of parameters must be Integer[3]{nbDigits, minRepeat, maxRepeat}");
                    throw new InvalidDigitParametersForMatchingMethod();
                }
                if ((nbDigits > 10 || nbDigits < 1) | (maxRepeat < 1 || minRepeat > maxRepeat || minRepeat < 0)) {
                    devLogger.fatal("Mandatory : parameter 1 nbDigits <= 10 && parameter 3 maxRepeat >=  parameter 2 minRepeat");
                    throw new InvalidDigitParametersForMatchingMethod();
                }
                // and other variables
                int[] digitCounter = {0,0,0,0,0,0,0,0,0,0};
                int inputDigit ;
                // create counter with integer array
                for (int i = 0 ; i < inputString.length() ; i++){
                    inputDigit = Integer.valueOf(String.valueOf(inputString.charAt(i)));
                    digitCounter[inputDigit] ++ ;
                }
                // compare each counter in array to check if it repects rule
                for (int i = 0 ; i < nbDigits; i++){
                    if (digitCounter[i] < minRepeat || digitCounter[i] > maxRepeat) isMatching = false;
                }
                break;
            default:
                devLogger.fatal("You can't use matchSpecificDigitRule method with undefined ruleName");
                throw new InvalidRuleNameForMatchingMethod();
        }
        return isMatching;
    }

}
