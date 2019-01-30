package com.herve.ocgames.utils;

import com.herve.ocgames.utils.exceptions.InvalidDigitParametersForMatchingMethod;
import com.herve.ocgames.utils.exceptions.InvalidInputStringForMatchingMethod;
import com.herve.ocgames.utils.exceptions.InvalidPatternForMatchingMethod;
import com.herve.ocgames.utils.exceptions.InvalidRuleNameForMatchingMethod;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class StringToolTest {

    private static Logger consoleLogger = Logger.getLogger("development_console");
    private static String logFileName = "log/OCGames_developpement.log";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private void clearDevelopmentLog(){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFileName));
            writer.write("");
            writer.close();
        } catch (IOException e) {
            consoleLogger.error("Can't clear development log before test - tests will not be relevant.");
        }
    }

    private boolean logFound(String logMessage){
        boolean result = false;
        try (BufferedReader br = new BufferedReader(new FileReader(logFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(logMessage)) result = true;
            }
        } catch (FileNotFoundException e) {
            consoleLogger.warn("Le fichier de log " + logFileName + " n'existe pas.");
            e.printStackTrace();
        } catch (IOException e) {
            consoleLogger.warn("Probleme de lecture du fichier de log " + logFileName + ".");
            e.printStackTrace();
        }
        return result;

    }

    @BeforeEach
    public void setUpStreams() {
        clearDevelopmentLog();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(System.out);
    }

    /*
    Test String.replaceAll
     */

    @Test
    public void Given_StringContainsStringToReplace_When_StringReplaceAll_Then_ModifyString(){
        String test = "je dors dans un lit en paille";
        test = test.replaceAll("paille", "pierre");
        assertEquals("je dors dans un lit en pierre", test);
    }

    /*
    Test String.replace
     */

    @Test
    public void Given_StringDoesntContainsStringToReplace_When_StringReplace_Then_DoNotModifyString(){
        String test = "je dors dans un lit en paille";
        test.replace("mousse", "pierre");
        assertEquals("je dors dans un lit en paille", test);
    }

    /*
    Tests  StringTool.arrayReplace
     */

    @Test
    public void Given_StringContainsStringToReplace_When_StringToolArrayReplace_Then_ModifyString(){
        // With valid and matching String input and vaild array, return modified string
        String correct = "je dors dans un lit en bois brun fabriqué par l'ébeniste";
        String test = "je mange dans un panier en pierre bleue peinte par le forgeron";
        String[][] substitutionTable = {{"mange", "dors"}, {"panier", "lit"}, {"pierre", "bois"}, {"bleue", "brun"},
                {"peinte","fabriqué"}, {"le forgeron","l'ébeniste"}};
        assertEquals(correct, StringTool.arrayReplace(test, substitutionTable));
    }

    @Test
    public void Given_StringDoesntContainStringToReplace_When_StringToolArrayReplace_Then_ReturnInputWithoutModifying(){
        // With valid and not matching String input and vaild array, return string without change
        String test = "je lis le soir avant de dormir";
        String[][] substitutionTable = {{"mange", "dors"}, {"panier", "lit"}, {"pierre", "bois"}, {"bleue", "brun"},
                {"peinte","fabriqué"}, {"le forgeron","l'ébeniste"}};
        assertEquals(test, StringTool.arrayReplace(test, substitutionTable));
    }

    @Test
    public void Given_StringAndEmptyArray_When_StringToolArrayReplace_Then_DoNothing(){
        // be sure to have no error and return empty String if array is empty
        String test = "je dors dans un lit en bois brun fabriqué par l'ébeniste";
        String[][] substitutionTable = {{"", ""}};
        assertEquals(test, StringTool.arrayReplace(test, substitutionTable));
    }

    @Test
    public void Given_StringAndNullArray_When_StringToolArrayReplace_Then_DoNothing(){
        // be sure to have no error and return Input string if array is null
        String test = "je dors dans un lit en bois brun fabriqué par l'ébeniste";
        String[][] substitutionTable = null;
        assertEquals(test, StringTool.arrayReplace(test, substitutionTable));
    }

    @Test
    public void Given_EmptyString_When_StringToolArrayReplace_Then_DoNothing(){
        // be sure to have no error and return empty String if Input string is empty
        String test = "";
        String[][] substitutionTable = {{"mange", "dors"}, {"panier", "lit"}, {"pierre", "bois"}, {"bleue", "brun"},
                {"peinte","fabriqué"}, {"le forgeron","l'ébeniste"}};
        assertEquals("", StringTool.arrayReplace(test, substitutionTable));
    }

    /*
    Tests StringTool.match
     */

    @Test
    public void Given_StringContainsMultipleEqualAndTheMatchingPattern_When_StringToolMatch_Then_ReturnTrue(){
        boolean isMatch = StringTool.match("====", "^={4,}$");
        assertTrue(isMatch);
    }

    @Test
    public void Given_StringWithSpecialCharsAndTheMatchingPattern_When_StringToolMatch_Then_ReturnTrue(){
        String currentString = "{\"operation\";(98+2)*45 -> 4500}";
        String pattern = "^\\{\\\"operation\\\";\\([0-9]{1,3}\\+[0-9]{1,2}\\)\\*[0-9]{1,4} -> [0-9]{1,}\\}$";
        boolean isMatch = StringTool.match(currentString, pattern);
        assertTrue(isMatch);
    }

    @Test
    public void Given_StringWithSpecialCharsAndNotMatchingPattern_When_StringToolMatch_Then_ReturnFalse(){
        String currentString = "(98+2)*45 -> 4500";
        String pattern = "([0-9]{1,3}+[0-9]{1,3})*100 -> [0-9]{1,9}";
        boolean isMatch = StringTool.match(currentString, pattern);
        assertFalse(isMatch);
    }

    @Test
    public void Given_EmptyInputString_When_StringToolMatch_Then_ReturnFalse(){
        boolean isMatch = StringTool.match("", "^[0-9]{1,}$");
        assertFalse(isMatch);
    }

    @Test
    public void Given_NullInputString_When_StringToolMatch_Then_TrowInvalidArgumentExceptionAndLogError(){
        assertThrows(InvalidInputStringForMatchingMethod.class,
                () -> StringTool.match(null, "^[0-9]{1,}$"));
        assertTrue(logFound("You can't use match method with null inputString"));
    }

    @Test
    public void Given_EmptyReferencePattern_When_StringToolMatch_Then_TrowExceptionAndLogError(){
        assertThrows(InvalidPatternForMatchingMethod.class,
                () -> StringTool.match("4R2Eef334", ""));
        assertTrue(logFound("You can't use match method with empty referencePattern"));
    }

    @Test
    public void Given_NullReferencePattern_When_StringToolMatch_Then_TrowExceptionAndLogError(){
        assertThrows(InvalidPatternForMatchingMethod.class,
                () -> StringTool.match("4R2Eef334", null));
        assertTrue(logFound("You can't use match method with null referencePattern"));
    }

    /*
    Tests StringTool.matchSpecificDigitRule
     */

    @Test
    public void Given_StringWithGoodNumberOccurences_When_MatchRuleDigitMaxRepeat_Then_ReturnTrue(){
        String currentString = "04351";
        boolean isMatch = StringTool.matchSpecificDigitRule(currentString,"digitMaxRepeat",new Integer[] {6,0,2});
        assertTrue(isMatch);
    }

    @Test
    public void Given_RightStringGoodParametersDispiteOf3rdIsHigh_When_MatchRuleDigitMaxRepeat_Then_ReturnTrue(){
        String currentString = "04354421";
        boolean isMatch = StringTool.matchSpecificDigitRule(currentString,"digitMaxRepeat",new Integer[] {6,0,50});
        assertTrue(isMatch);
    }

    @Test
    public void Given_StringWithMoreThanMaxNumberOccurence_When_MatchRuleDigitMaxRepeat_Then_ReturnFalse(){
        String currentString = "0434421";
        boolean isMatch = StringTool.matchSpecificDigitRule(currentString,"digitMaxRepeat",new Integer[] {8,0,2});
        assertFalse(isMatch);
    }

    @Test
    public void Given_StringWithLessThanMinNumberOccurences_When_MatchRuleWithMinMax_Then_ReturnFalse(){
        String currentString = "04351430297";
        boolean isMatch = StringTool.matchSpecificDigitRule(currentString,"digitMaxRepeat",new Integer[] {10,1,2});
        assertFalse(isMatch);
    }

    @Test
    public void Given_NullInputString_When_StringToolMatchSpecificRule_Then_TrowExceptionAndLogError(){
        assertThrows(InvalidInputStringForMatchingMethod.class,
                () -> StringTool.matchSpecificDigitRule(null,"digitMaxRepeat",new Integer[] {10,1,2}));
        assertTrue(logFound("You can't use matchSpecificDigitRule method with null inputString"));
    }

    @Test
    public void Given_StringWithOtherCharThanDigit_When_MatchRuleDigitMaxRepeat_Then_TrowExceptionAndLogError(){
        String currentString = "AVDC012";
        assertThrows(InvalidInputStringForMatchingMethod.class,
                () -> StringTool.matchSpecificDigitRule(currentString,"digitMaxRepeat",new Integer[] {6,0,2}));
        assertTrue(logFound("You can't use matchSpecificDigitRule method if inputString doesn't contain only digits"));
    }

    @Test
    public void Given_NullRuleName_When_StringToolMatchSpecificRule_Then_TrowInvalidArgumentExceptionAndLogError(){
        String currentString = "04351430297";
        assertThrows(InvalidRuleNameForMatchingMethod.class,
                () -> StringTool.matchSpecificDigitRule(currentString,null,new Integer[] {10,1,2}));
        assertTrue(logFound("You can't use matchSpecificDigitRule method with null ruleName"));
    }

    @Test
    public void Given_NullReferencePattern_When_StringToolMatchSpecificRule_Then_TrowInvalidArgumentExceptionAndLogError(){
        String currentString = "04351430297";
        assertThrows(InvalidRuleNameForMatchingMethod.class,
                () -> StringTool.matchSpecificDigitRule(currentString,"",new Integer[] {10,1,2}));
        assertTrue(logFound("You can't use matchSpecificDigitRule method with undefined ruleName"));
    }

    @Test
    public void Given_InvalidArray_When_StringToolMatchSpecificMaxRepeatRule_Then_TrowExceptionAndLogError(){
        String currentString = "04351430297";
        assertThrows(InvalidDigitParametersForMatchingMethod.class,
                () -> StringTool.matchSpecificDigitRule(currentString,"digitMaxRepeat",new Integer[] {0,1}));
        assertTrue(logFound("Array of parameters must be Integer[3]{nbDigits, minRepeat, maxRepeat}"));
    }

    @Test
    public void Given_FirstArrayValueTooLow_When_StringToolMatchSpecificMaxRepeatRule_Then_TrowExceptionAndLogError(){
        String currentString = "021";
        assertThrows(InvalidDigitParametersForMatchingMethod.class,
                () -> StringTool.matchSpecificDigitRule(currentString,"digitMaxRepeat",new Integer[] {0,1,2}));
        assertTrue(logFound("Mandatory : parameter 1 nbDigits <= 10 && parameter 3 maxRepeat >=  parameter 2 minRepeat"));
    }

    @Test
    public void Given_FirstArrayValueTooHigh_When_StringToolMatchSpecificMaxRepeatRule_Then_TrowExceptionAndLogError(){
        String currentString = "021";
        assertThrows(InvalidDigitParametersForMatchingMethod.class,
                () -> StringTool.matchSpecificDigitRule(currentString,"digitMaxRepeat",new Integer[] {12,0,2}));
        assertTrue(logFound("Mandatory : parameter 1 nbDigits <= 10 && parameter 3 maxRepeat >=  parameter 2 minRepeat"));
    }

    @Test
    public void Given_2ndParameterHigherThanThird_When_StringToolMatchSpecificMaxRepeatRule_Then_TrowExceptionAndLogError(){
        String currentString = "021473";
        assertThrows(InvalidDigitParametersForMatchingMethod.class,
                () -> StringTool.matchSpecificDigitRule(currentString,"digitMaxRepeat",new Integer[] {8,3,2}));
        assertTrue(logFound("Mandatory : parameter 1 nbDigits <= 10 && parameter 3 maxRepeat >=  parameter 2 minRepeat"));
    }

    @Test
    public void Given_Negative2ndParameter_When_StringToolMatchSpecificMaxRepeatRule_Then_TrowExceptionAndLogError(){
        String currentString = "021473";
        assertThrows(InvalidDigitParametersForMatchingMethod.class,
                () -> StringTool.matchSpecificDigitRule(currentString,"digitMaxRepeat",new Integer[] {8,-1,2}));
        assertTrue(logFound("Mandatory : parameter 1 nbDigits <= 10 && parameter 3 maxRepeat >=  parameter 2 minRepeat"));
    }

    @Test
    public void Given_Zero3rdParameter_When_StringToolMatchSpecificMaxRepeatRule_Then_TrowExceptionAndLogError(){
        String currentString = "021473";
        assertThrows(InvalidDigitParametersForMatchingMethod.class,
                () -> StringTool.matchSpecificDigitRule(currentString,"digitMaxRepeat",new Integer[] {8,0,0}));
        assertTrue(logFound("Mandatory : parameter 1 nbDigits <= 10 && parameter 3 maxRepeat >=  parameter 2 minRepeat"));
    }


}