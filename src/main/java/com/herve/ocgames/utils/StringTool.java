package com.herve.ocgames.utils;

import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTool {

    private static Logger supportLogger = Logger.getLogger("support_file");
    private static Logger devLogger = Logger.getLogger("development_file");

    /**
     * A home matcher to avoid to repeat this 3 lines again and again
     * @param inputString String on which we seach pattern
     * @param referencePattern this String will used to build pattern
     * @return true if matches, else false
     */
    public static boolean match(String inputString, String referencePattern) {
        Pattern p = Pattern.compile(referencePattern);
        Matcher m = p.matcher(inputString);
        return m.matches();
    }

    /**
     * A tool for massive substitutions
     * @param inputString String on which we apply arrayReplace
     * @param arraySubstitutions String array in 2 dimensions which contains several pairs {String to search, substitute}
     * @return String after all substitutions
     */
    public static String arrayReplace(String inputString, String[][] arraySubstitutions) {
        String newStringValue = inputString;
        for (String[] substitution : arraySubstitutions) {
            newStringValue = newStringValue.replaceAll(substitution[0], substitution[1]);
        }
        return newStringValue;
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
        // switch on all rulename, and default throw exception
        switch (ruleName){
            case "digitMaxRepeat":
                // Rules to control times we repeat a digit in String only composed with digits from 0 to nbDigit - 1
                // numeric parameters
                int nbDigits = numericParameters[0];
                int minRepeat = numericParameters[1];
                int maxRepeat = numericParameters[2];
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
                // on devrait créé une exception invalid ruleName
        }
        return isMatching;
    }

}
