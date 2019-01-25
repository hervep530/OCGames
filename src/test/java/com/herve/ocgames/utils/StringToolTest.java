package com.herve.ocgames.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class StringToolTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(System.out);
    }

    @Test
    public void Given_StringContainsStringToReplace_When_StringReplaceAll_Then_ModifyString(){
        String test = "je dors dans un lit en paille";
        test = test.replaceAll("paille", "pierre");
        assertEquals("je dors dans un lit en pierre", test);
    }

    @Test
    public void Given_StringDoesntContainsStringToReplace_When_StringReplace_Then_DoNotModifyString(){
        String test = "je dors dans un lit en paille";
        test.replace("mousse", "pierre");
        assertEquals("je dors dans un lit en paille", test);
    }

    @Test
    public void Given_StringContainsStringToReplace_When_StringOperationsArrayReplace1Array_Then_ModifyString(){
        String correct = "je dors dans un lit en bois brun fabriqué par l'ébeniste";
        String test = "je mange dans un panier en pierre bleue peinte par le forgeron";
        String[][] substitutionTable = {{"mange", "dors"}, {"panier", "lit"}, {"pierre", "bois"}, {"bleue", "brun"},
                {"peinte","fabriqué"}, {"le forgeron","l'ébeniste"}};
        assertEquals(correct, StringTool.arrayReplace(test, substitutionTable));
    }

    @Test
    public void Given_StringContainsMultipleEqualAndTheMatchingPattern_When_StringOperationsMatch_Then_ReturnTrue(){
        boolean isMatch = StringTool.match("====", "^={4,}$");
        assertTrue(isMatch);
    }

    @Test
    public void Given_StringWithSpecialCharsAndTheMatchingPattern_When_StringOperationsMatch_Then_ReturnTrue(){
        String currentString = "{\"operation\";(98+2)*45 -> 4500}";
        String pattern = "^\\{\\\"operation\\\";\\([0-9]{1,3}\\+[0-9]{1,2}\\)\\*[0-9]{1,4} -> [0-9]{1,}\\}$";
        boolean isMatch = StringTool.match(currentString, pattern);
        assertTrue(isMatch);
    }

    @Test
    public void Given_StringWithSpecialCharsAndNotMatchingPattern_When_StringOperationsMatch_Then_ReturnFalse(){
        String currentString = "(98+2)*45 -> 4500";
        String pattern = "([0-9]{1,3}+[0-9]{1,3})*100 -> [0-9]{1,9}";
        boolean isMatch = StringTool.match(currentString, pattern);
        assertFalse(isMatch);
    }

    @Test
    public void Given_StringWithGoodNumberOccurences_When_MatchSpecificRuleWithMinMax_Then_ReturnTrue(){
        String currentString = "04351";
        boolean isMatch = StringTool.matchSpecificDigitRule(currentString,"digitMaxRepeat",new Integer[] {6,0,2});
        assertTrue(isMatch);
    }

    @Test
    public void Given_StringWithMoreThanMaxNumberOccurence_When_MatchSpecificRuleWithMinMax_Then_ReturnFalse(){
        String currentString = "0434421";
        boolean isMatch = StringTool.matchSpecificDigitRule(currentString,"digitMaxRepeat",new Integer[] {8,0,2});
        assertFalse(isMatch);
    }

    @Test
    public void Given_StringWithLessThanMinNumberOccurences_When_MatchSpecificRuleWithMinMax_Then_ReturnFalse(){
        String currentString = "04351430297";
        boolean isMatch = StringTool.matchSpecificDigitRule(currentString,"digitMaxRepeat",new Integer[] {10,1,2});
        assertFalse(isMatch);
    }

}