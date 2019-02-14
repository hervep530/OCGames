package com.herve.ocgames.utils;

import com.herve.ocgames.core.PropertyHelper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class UserInteraction {

    private static Scanner sc = new Scanner(System.in);
    private static final Logger supportLogger = LogManager.getLogger("support_file");
    private static final Logger devConsoleLogger = LogManager.getLogger("development_console");
    private static final Logger dev = LogManager.getLogger("development_file");
    private static boolean loggerInitialized = false;
    private static final Level VALUE = Level.getLevel("VALUE");
    private static final Level COMMENT = Level.getLevel("COMMENT");
    private static final Level LOOP = Level.getLevel("LOOP");
    private static Level debugVerbosity = Level.getLevel("WARN");

    /**
     * Set debug level (VALUE / COMMENT / LOOP)
     */
    private static void initLogger(){
        Configurator.setLevel(dev.getName(), debugVerbosity);
        loggerInitialized = true;
    }



    /**
     * Static method - Ask input as integer to select one of multiple proposal as menu
     * @param category  Category of proposal
     * @param responses String array of possible answers
     * @param showResponse  Boolean - true to display response for confirmation
     * @return
     */
    public static int promptSelect(String category, String[] responses, boolean showResponse) {
        if ( ! loggerInitialized ) initLogger();
        //Display menu title and list of choice
        String[][] langSubstitutes = new String[][] {{"VAR_CATEGORY", category}};
        System.out.print(String.format(lang("select.title", langSubstitutes)));
        for (int i = 1; i <= responses.length; i++)
            System.out.println(i + " - " + responses[i - 1]);
        System.out.print(String.format(lang("select.question", langSubstitutes)));
        int nbResponse;
        // Use scanner while user response is not matching with rules
        boolean responseIsGood;
        do {
            try {
                // scanner
                nbResponse = sc.nextInt();
            } catch (InputMismatchException e) {
                // exception - so put value out of range and re-initialize scanner
                nbResponse = -1;
                sc.next();
            } catch (NoSuchElementException e) {
                // exception - so put value out of range and re-initialize scanner
                nbResponse = -1;
                sc.next();
            }
            // check if answer is inside the wanted range, so we will exit loop do ... until if true
            responseIsGood = (nbResponse >= 1 && nbResponse <= responses.length);
            if (responseIsGood) {
                // Valid answer confirmation if showResponse is true
                if (showResponse) {
                    String[][] answerSubstitutes = new String[][]{
                            {"VAR_CATEGORY", category},
                            {"VAR_ANSWER", responses[nbResponse - 1]}};
                    System.out.print(String.format(lang("select.rightAnswer", langSubstitutes)));
                }
            } else {
                // Invalid answer (we won't exit loop) and we ask player to try again
                boolean isVowel = "aeiouy".contains(Character.toString(category.charAt(0)));
                if (isVowel)
                    System.out.print(String.format(lang("select.invalidCategoryVowel", langSubstitutes)));
                else
                    System.out.print(String.format(lang("select.invalidCategoryConsonant", langSubstitutes)));
            }
        } while (!responseIsGood);
        // 0 will never been returned because we use nbResponse instead of (nbResponse - 1) - 0 is reserved for "exit"
        return nbResponse;
    }


    /**
     * Static method - Ask input as string and check if answer matches rules
     * @param question   Text for the question to display
     * @param patternInput  Pattern (regexp) to define and check rule about input
     * @param showResponse  Display message to confirm user answer
     * @return the string give as input
     */
    public static String promptInput(String question, String patternInput, String color, boolean showResponse) {
        if ( ! loggerInitialized ) initLogger();
        //Display question with using display message method, instanciate response and compile pattern
        UserInteraction.displayMessage(question, color);
        String strResponse;
        Pattern pattern = Pattern.compile(patternInput);

        // Use scanner while user response is not matching with rules
        boolean responseIsGood = false;
        do {
            strResponse ="";
            try {
                // scanner
                strResponse = sc.next();
            } catch (NoSuchElementException e) {
                strResponse = "";
            }
            if (StringTool.match(strResponse, patternInput)) {
                // Valid answer, so we will exit loop do ... until
                String[][] langSubstitutes = new String[][]{{"VAR_RESPONSE",strResponse}};
                if (showResponse) System.out.print(String.format(lang("input.validAnswer", langSubstitutes)));
                responseIsGood = true;
            } else {
                // Invalid answer, so we won't exit loop - only ask player to try again
                System.out.print(String.format(lang("input.invalidAnswer")));
            }
        } while (!responseIsGood);
        // return input value
        return strResponse;
    }


    /**
     * Propose la saisie d'une chaîne de caractère avec contrôle de validité
     * @param question   Texte de la question à afficher
     * @param patternInput  Pattern du texte attendu (regexp)
     * @param color Color for message displayed as defined in text (can be given in lowercase)
     * @param specificRuleName Name of the specific rule used to check if the answer match or not
     * @param ruleParameters digit parameters for speficific rule in array Integer[]
     * @param showResponse  boolean - display answer to confirm input if true
     * @return
     */
    public static String promptInput(String question, String patternInput, String color, String specificRuleName, Integer[] ruleParameters, boolean showResponse) {
        if ( ! loggerInitialized ) initLogger();
        //System.out.print(String.format(question));
        UserInteraction.displayMessage(question, color);
        String strResponse;
        Pattern pattern = Pattern.compile(patternInput);
        boolean responseIsGood;
        do {
            strResponse ="";
            responseIsGood = true;
            try {
                strResponse = sc.next();
            } catch (NoSuchElementException e) {
                strResponse = "";
            }
            if (! StringTool.match(strResponse, patternInput) ||
                    ! StringTool.matchSpecificDigitRule(strResponse, specificRuleName, ruleParameters))
                responseIsGood = false;
            if (responseIsGood) {
                String[][] langSubstitutes = new String[][]{{"VAR_RESPONSE",strResponse}};
                if (showResponse) System.out.print(String.format(lang("input.validAnswer", langSubstitutes)));
            } else {
                System.out.print(String.format(lang("input.invalidAnswer")));
            }
        } while (!responseIsGood);
        return strResponse;
    }

    /**
     * re-instanciate scanner - useful in Test
     */
    public static void initializeScanner(){
        sc = new Scanner(System.in);
    }

    /*
     * Several version of displayMessage are implemented
     */

    /**
     * Display message from given text (shorcut for sysout)
     * @param message text to display
     */
    public static void displayMessage(String message) {
        System.out.print(String.format(message));
    }

    /**
     * Same as previous, but with color
     * @param message text to display
     * @param textColor text color as defined in text (lowercase is used for this argument)
     */
    public static void displayMessage(String message, String textColor) {
        System.out.print(String.format(Text.effect(message, textColor)));
    }

    /**
     * Display message from given response object
     * @param response response object which may contain error message and / or standard message
     */
    public static void displayMessage(Response response) {
        String message = "";
        // Priority is given to error message, then get standard message
        message = response.getErrMessage();
        if (message.contentEquals("")) message = response.getMessage();
        // If message is not "", it will be displayed
        if ( ! message.contentEquals("")) System.out.print(String.format(message));
    }

    /**
     * Same as previous, but with color
     * @param response response object which may contain error message and / or standard message
     * @param textColor text color as defined in text (lowercase is used for this argument)
     */
    public static void displayMessage(Response response, String textColor) {
        String message = "";
        // Priority is given to error message, then get standard message
        message = response.getErrMessage();
        if (message.contentEquals("")) message = Text.effect(response.getMessage(),textColor);
        // If message is not "", it will be displayed
        if ( ! message.contentEquals("")) System.out.print(String.format(message));
    }

    /**
     * Shortcut to get PropertyHelper languages values (messages)
     * @param key (message key as String)
     * @return message in choosen language
     */
    private static String lang(String key){
        return PropertyHelper.language(key);
    }

    /**
     * Shortcut to get PropertyHelper languages values (messages)
     * @param key (message key as String)
     * @param arraySubstitutions 2 dimensions array to substitute string array[i][0] by array[i][1]
     * @return message in choosen language
     */
    private static String lang(String key, String[][] arraySubstitutions){
        return PropertyHelper.language(key, arraySubstitutions);
    }

    /**
     * Console feature tried to test text arrangement (finally not used for the moment)
     */
    public static void clearConsole(){
        System.out.print(String.format(Text.CLEAR_SCREEN));
    }

}
