package com.herve.ocgames.utils;

import com.herve.ocgames.core.PropertyHelper;
import org.apache.log4j.Logger;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class UserInteraction {

    private static Scanner sc = new Scanner(System.in);
    private static Logger supportLogger = Logger.getLogger("support_file");
    private static Logger devLogger = Logger.getLogger("development_file");



    /**
     * Method static pour proposer la saisie d'un choix
     * @param category  Category du choix
     * @param responses Tableau des réponses possibles
     * @param showResponse  Affichage d'une confirmation de la réponse
     * @return
     */
    public static int promptSelect(String category, String[] responses, boolean showResponse) {
        String[][] langSubstitutes = new String[][] {{"VAR_CATEGORY", category}};
        System.out.print(String.format(lang("select.title", langSubstitutes)));
        for (int i = 1; i <= responses.length; i++)
            System.out.println(i + " - " + responses[i - 1]);
        System.out.print(String.format(lang("select.question", langSubstitutes)));
        int nbResponse;
        boolean responseIsGood;
        do {
            try {
                nbResponse = sc.nextInt();
            } catch (InputMismatchException e) {
                nbResponse = -1;
                sc.next();
            } catch (NoSuchElementException e) {
                nbResponse = -1;
                sc.next();
            }
            responseIsGood = (nbResponse >= 1 && nbResponse <= responses.length);
            if (responseIsGood) {
                if (showResponse) {
                    String[][] answerSubstitutes = new String[][]{
                            {"VAR_CATEGORY", category},
                            {"VAR_ANSWER", responses[nbResponse - 1]}};
                    System.out.print(String.format(lang("select.rightAnswer", langSubstitutes)));
                }
            } else {
                boolean isVowel = "aeiouy".contains(Character.toString(category.charAt(0)));
                if (isVowel)
                    System.out.print(String.format(lang("select.invalidCategoryVowel", langSubstitutes)));
                else
                    System.out.print(String.format(lang("select.invalidCategoryConsonant", langSubstitutes)));
            }
        } while (!responseIsGood);
        // On ne retourne par nbResponse - 1 . On ne retourne donc jamais la valeur 0
        return nbResponse;
    }


    /**
     * Propose la saisie d'une chaîne de caractère avec contrôle de validité
     * @param question   Texte de la question à afficher
     * @param patternInput  Pattern du texte attendu (regexp)
     * @param showResponse  Affichage de la confirmation de la réponse
     * @return
     */
    public static String promptInput(String question, String patternInput, String color, boolean showResponse) {
        //System.out.print(String.format(question));
        UserInteraction.displayMessage(question, color);
        String strResponse;
        Pattern pattern = Pattern.compile(patternInput);

        boolean responseIsGood = false;
        do {
            strResponse ="";
            try {
                strResponse = sc.next();
            } catch (NoSuchElementException e) {
                strResponse = "";
                //sc.nextLine();
            }
            if (StringTool.match(strResponse, patternInput)) {
                String[][] langSubstitutes = new String[][]{{"VAR_RESPONSE",strResponse}};
                if (showResponse) System.out.print(String.format(lang("input.validAnswer", langSubstitutes)));
                responseIsGood = true;
            } else {
                System.out.print(String.format(lang("input.invalidAnswer")));
            }
        } while (!responseIsGood);
        return strResponse;
    }


    public static String promptInput(String question, String patternInput, String color, String specificRuleName, Integer[] ruleParameters, boolean showResponse) {
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

    public static void initializeScanner(){
        sc = new Scanner(System.in);
    }

    public static void displayMessage(Response response) {
        String message = "";
        // On prend en priorité le message d'erreur, puis le message applicatif
        message = response.getErrMessage();
        if (message.contentEquals("")) message = response.getMessage();
        // S'il y a un message on l'affiche
        if ( ! message.contentEquals("")) System.out.print(String.format(message));
    }

    public static void displayMessage(Response response, String textColor) {
        String message = "";
        // On prend en priorité le message d'erreur, puis le message applicatif
        message = response.getErrMessage();
        if (message.contentEquals("")) message = Text.effect(response.getMessage(),textColor);
        // S'il y a un message on l'affiche
        if ( ! message.contentEquals("")) System.out.print(String.format(message));
    }

    public static void clearConsole(){
        System.out.print(String.format(Text.CLEAR_SCREEN));
    }

    public static void displayMessage(String message) {
        System.out.print(String.format(message));
    }

    public static void displayMessage(String message, String textColor) {
        System.out.print(String.format(Text.effect(message, textColor)));
    }

    private static String lang(String key){
        return PropertyHelper.language(key);
    }

    private static String lang(String key, String[][] arraySubstitutions){
        return PropertyHelper.language(key, arraySubstitutions);
    }



}
