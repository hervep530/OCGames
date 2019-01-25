package com.herve.ocgames.utils;

import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTool {

    private static Logger supportLogger = Logger.getLogger("support_file");
    private static Logger devLogger = Logger.getLogger("development_file");

    /**
     * Juste un matcher maison par flemme de répeter toujours les mêmes 3 lignes de code
     * @param inputString
     * @param referencePattern
     * @return
     */
    public static boolean match(String inputString, String referencePattern) {
        Pattern p = Pattern.compile(referencePattern);
        Matcher m = p.matcher(inputString);
        return m.matches();
    }

    /**
     * Un arrayReplace pour remplacement en masse, avec en paramètre la chaine à traiter et un tableau à 2 dimensions
     * @param inputString La chaîne à traiter
     * @param arraySubstitutions Tableau à deux dimensions [n][2] : la 1ere est l'index des subs, la 2e stocke la valeur à remplacer en
     *                           index 0 (array[i][0]), et la valeur de remplacement en index 1(array[i][1]
     * @return La nouvelle chaîne après remplacement
     */
    public static String arrayReplace(String inputString, String[][] arraySubstitutions) {
        String newStringValue = inputString;
        for (String[] substitution : arraySubstitutions) {
            newStringValue = newStringValue.replaceAll(substitution[0], substitution[1]);
        }
        return newStringValue;
    }

    /**
     * Surcharge à implémenter : Un arrayReplace avec la chaîne à traiter et deux tableaux en paramètres au lieu
     * d'un seul à 2 dimensions - Attention à bien faire correspondre les index des 2 tableaux!
     * @param inputString La chaîne à traiter
     * @param arrayToReplace Le tableau des valeurs à remplacer
     * @param arraySubstitute Le tableau des valeurs de substitution
     * @return
     */
    public static String arrayReplace(String inputString, String[] arrayToReplace, String[] arraySubstitute) {
        return "";
    }

    /*
    // Methode inutile
    public static String replace(String inputString, String toReplace, String substitute) {
        return inputString.replaceAll(toReplace, substitute);
    }
     */

    /**
     * A implémenter au besoin - Un matcher custom avec en entrée la chaine à controler et le nom de la règle sans autre paramètre
     * @param inputString Chaine à traiter
     * @param ruleName Nom de la règle pour controler si ça matche (on fait un switch sur les noms de regles)
     * @return True, ça matche, False, ça matche pas
     */
    public static boolean matchSpecificRule (String inputString, String ruleName){
        return true;
    }

    /**
     * Matcher Custom avec surcharge
     * @param inputString La chaine a traiter
     * @param ruleName Le nom de la regle
     * @param nbChips
     * @param nbMin
     * @param nbMax
     * @return
     */
    public static boolean matchSpecificRule (String inputString, String ruleName,int nbChips, int nbMin, int nbMax){
        // Par défaut on dit que ça matche
        boolean isMatching = true;
        // On switche sur les ruleName et on throw une exception si la rule n'existe pas
        switch (ruleName){
            case "minMaxOfDigit":
                // Regle pour controler la répétition de chiffres dans une chaine ne contenant que des chiffres
                int[] digitCounter = {0,0,0,0,0,0,0,0,0,0};
                int inputDigit ;
                for (int i = 0 ; i < inputString.length() ; i++){
                    inputDigit = Integer.valueOf(String.valueOf(inputString.charAt(i)));
                    digitCounter[inputDigit] ++ ;
                }
                for (int i = 0 ; i < nbChips; i++){
                    if (digitCounter[i] < nbMin || digitCounter[i] > nbMax) isMatching = false;
                }
                break;
            default:
                // on devrait créé une exception invalid ruleName
        }
        return isMatching;
    }

}
