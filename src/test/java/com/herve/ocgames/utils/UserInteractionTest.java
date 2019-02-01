package com.herve.ocgames.utils;

import com.herve.ocgames.core.PropertyHelper;
import com.herve.ocgames.utils.UserInteraction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class UserInteractionTest {


    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(System.out);
    }


    protected String lang(String key, String[][] arraySubstitutions){
        return PropertyHelper.language(key, arraySubstitutions);
    }

    private String validAnswer(){
        return "";
    }

    private String invalidAnswer(){
        return "";
    }

    @Test
    void Given_ResponseNotMatchingAndResponseMatching_When_AskCodeWithPromptInput_Then_DisplayErrorAndGoodResponse (){
        System.setIn(new ByteArrayInputStream("123soleil\n135malin\n".getBytes()));
        String strResponse = UserInteraction.promptInput("Quel est le code secret?",
                ".*3[0-9]\\w*li.*", "reset", true);
        String[] output = outContent.toString().replace("\r\n", "\n").split("\n");
        assertEquals(true, output[0].contains("code secret"));
        assertEquals("Cette tentative n'est pas valide. Veuillez effectuez une nouvelle saisie.", output[1]);
        assertEquals("Votre nouvelle tentative est 135malin", output[2]);
    }

    @Test
    void Given_BadResponseAndResponse4_When_AskAboutColorsWithFiveResponses_Then_DisplayErrorAndGoodResponse() {
        System.setIn(new ByteArrayInputStream("12\n4\n".getBytes()));
        String[] responses = {"Rouge", "Bleu", "Jaune", "Vert", "Gris"};
        int intResponse = UserInteraction.promptSelect("couleur", responses, true);
        String[] output = outContent.toString().replace("\r\n", "\n").split("\n");
        assertEquals(true, output[0].contains("couleur"));
        assertEquals("Vous n'avez pas choisi de couleur parmi les choix proposés", output[7]);
        assertEquals("Vous avez choisi comme couleur : Vert", output[8]);
    }

    @Test
    void Given_3NotMatchingInputAndOneGood_When_PromptInput5ArithmeticSymbols_Then_Display3ErrorAndGoodResponse() {
        System.setIn(new ByteArrayInputStream("44444\n++-=\naeixoee\n-==++\n".getBytes()));
        String strResponse = UserInteraction.promptInput("Evaluez la tentative avec + = et - ?",
                "^[-\\+=]{5}$", "reset", true);
        String[] output = outContent.toString().replace("\r\n", "\n").split("\n");
        assertTrue(output[0].contains("Evaluez la tentative"));
        assertTrue(output[1].contains("pas valide."));
        assertTrue(output[2].contains("pas valide."));
        assertTrue(output[3].contains("pas valide."));
        assertEquals("Votre nouvelle tentative est -==++", output[4]);
    }

    @Test
    void Given_InputWithDigitsOccurencesInRange_When_PromptInput_Then_GoodResponse (){
        System.setIn(new ByteArrayInputStream("35723\n".getBytes()));
        String question = "Quel est le code secret?";
        String matchingPattern = "[0-7]{5}";
        String strResponse = UserInteraction.promptInput(question, matchingPattern, "reset",
                "digitMaxRepeat", new Integer[]{8,0,2},true);
        String[] output = outContent.toString().replace("\r\n", "\n").split("\n");
        assertTrue(output[0].contains("code secret"));
        assertEquals("Votre nouvelle tentative est 35723", output[1]);
    }

    @Test
    void Given_InputWithDigitsOccurencesLessThanMinAndInRange_When_PromptInput_Then_AskRetryAndGoodResponse (){
        System.setIn(new ByteArrayInputStream("35221003\n01234545\n".getBytes()));
        String question = "Quel est le code secret?";
        String matchingPattern = "[0-5]{8}";
        String strResponse = UserInteraction.promptInput(question, matchingPattern, "reset",
                "minMaxOfDigit", new Integer[]{6,1,2},true);
        String[] output = outContent.toString().replace("\r\n", "\n").split("\n");
        assertTrue(output[0].contains("code secret"));
        assertEquals("Cette tentative n'est pas valide. Veuillez effectuez une nouvelle saisie.", output[1]);
        assertEquals("Votre nouvelle tentative est 01234545", output[2]);
    }

    @Test
    void Given_InputWithDigitsOccurencesMoreThanMaxAndInRange_When_PromptInput_Then_AskRetryAndGoodResponse (){
        System.setIn(new ByteArrayInputStream("3537823\n354721\n".getBytes()));
        String question = "Quel est le code secret?";
        String matchingPattern = "[0-7]{6}";
        String strResponse = UserInteraction.promptInput(question, matchingPattern, "reset",
                "minMaxOfDigit", new Integer[]{8,0,2},true);
        String[] output = outContent.toString().replace("\r\n", "\n").split("\n");
        assertTrue(output[0].contains("code secret"));
        assertEquals("Cette tentative n'est pas valide. Veuillez effectuez une nouvelle saisie.", output[1]);
        assertEquals("Votre nouvelle tentative est 354721", output[2]);
    }



}