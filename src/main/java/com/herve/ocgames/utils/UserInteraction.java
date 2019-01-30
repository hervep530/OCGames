package com.herve.ocgames.utils;

import org.apache.log4j.Logger;

import java.util.Scanner;

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
        return 0;
    }


    /**
     * Propose la saisie d'une chaîne de caractère avec contrôle de validité
     * @param question   Texte de la question à afficher
     * @param patternInput  Pattern du texte attendu (regexp)
     * @param showResponse  Affichage de la confirmation de la réponse
     * @return
     */
    public static String promptInput(String question, String patternInput, boolean showResponse) {
        return "";
    }

    public static String promptInput(String question, String patternInput, String specificRuleName, Integer[] ruleParameters, boolean showResponse) {
        return "";
    }

    public static void displayMessage(Response response) {
        return;
    }

    public static void displayMessage(Response response, String textColor) {
        return;
    }

    public static void displayMessage(String message) {
        System.out.println(String.format(message));
    }

    public static void displayMessage(String message, String textColor) {
        System.out.println(String.format(textColor + message + Text.RESET));
    }


}
