package com.herve.ocgames.mastermind;

import com.herve.ocgames.core.CodeGenerator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class CodeGeneratorMasterMindTest {
    private static Logger consoleLogger = Logger.getLogger("development_console");
    private static String devLogFile = "log/OCGames_developpement.log";
    private static Level devLogLevel = Logger.getLogger("development_file").getLevel();
    private static String supportLogFile = "log/OCGames.log";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private void clearDevelopmentLog(){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(devLogFile));
            writer.write("");
            writer.close();
        } catch (IOException e) {
            consoleLogger.error("Can't clear development log before test - tests will not be relevant.");
        }
    }

    private boolean logFound(String logMessage, String fileName){
        boolean result = false;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(logMessage)) result = true;
            }
        } catch (FileNotFoundException e) {
            consoleLogger.warn("Le fichier de log " + fileName + " n'existe pas.");
            e.printStackTrace();
        } catch (IOException e) {
            consoleLogger.warn("Probleme de lecture du fichier de log " + fileName + ".");
            e.printStackTrace();
        }
        return result;

    }

    @BeforeEach
    public void setUpStreams() {
        clearDevelopmentLog();
        Logger.getRootLogger().addAppender(Logger.getLogger("development_file").getAppender("devFile"));
        Logger.getLogger("development_file").setLevel(Level.DEBUG);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        Logger.getRootLogger().removeAppender(Logger.getLogger("development_file").getAppender("devFile"));
        Logger.getLogger("development_file").setLevel(devLogLevel);
        System.setOut(System.out);
    }


    /*
     * Tests d'instanciation de la classe CodeGeneratorPlusMoins
     * si l'on a un argument code, ça genere l'objet avec ce code en fixe, sinon c'est un random

    @Test
    public void Given_CodeWithGenerateNotRandom_When_CreateNewCodeGenerator_Then_GetCodeReturnTheGivenCode() {
        int[] gameLevel = {4,12,6};
        CodeGenerator codeGenerator = new CodeGeneratorMasterMind(gameLevel, "4714");
        assertEquals("4714", codeGenerator.getCode());
    }
     */

    /*
     * Tests de la methode generateRandom

    @Test
    public void Given_CodeGeneratorCreateWithoutRandom_When_GenerateRandom_Then_GetCodeReturnTheRandomCode() {
        int[] gameLevel = {5,12,8};
        CodeGenerator codeGenerator = new CodeGeneratorMasterMind(gameLevel, "32482");
        codeGenerator.generateRandom();
        assertEquals(false, codeGenerator.getCode().contentEquals("32482"));
        assertEquals(true, StringOperations.match(codeGenerator.getCode(),"^[0-9]{5}$"));
    }

    @Test
    public void Given_2CodeGeneratorPlusMoins_When_GenerateRandom_Then_Get2DifferentCodesWith6Digit() {
        int[] gameLevel = {6,12,10};
        CodeGenerator codeGenerator1 = new CodeGeneratorMasterMind(gameLevel);
        CodeGenerator codeGenerator2 = new CodeGeneratorMasterMind(gameLevel);
        codeGenerator1.generateRandom();
        codeGenerator2.generateRandom();
        assertEquals(true, StringOperations.match(codeGenerator1.getCode(),"^[0-9]{6}$"));
        assertEquals(true, StringOperations.match(codeGenerator2.getCode(),"^[0-9]{6}$"));
        assertEquals(false, codeGenerator1.getCode().contentEquals(codeGenerator2.getCode()));
    }
     */


    /*
     * Tests de la methode askSecretCode

    @Test
    public void Given_CodeMatchingRules_When_AskPlayerSecretCode_Then_ReturnAValidRefereeCode() {
        System.setIn(new ByteArrayInputStream("70623\n".getBytes()));
        consoleUI = new UserInteraction();
        int[] gameLevel = {5,12,8};
        CodeGenerator codeGenerator = new CodeGeneratorMasterMind(gameLevel);
        Response response = codeGenerator.askPlayerSecretCode(consoleUI);
        assertEquals("70623", response.getValue("code"));
    }

    @Test
    public void Given_CodeWithWrongLength_When_AskPlayerSecretCode_Then_AskCodeUntilRulesSatisfied() {
        System.setIn(new ByteArrayInputStream("80312\n803912\n".getBytes()));
        consoleUI = new UserInteraction();
        int[] gameLevel = {6,12,10};
        CodeGenerator codeGenerator = new CodeGeneratorMasterMind(gameLevel);
        Response response = codeGenerator.askPlayerSecretCode(consoleUI);
        assertEquals("803912", response.getValue("code"));
    }

    @Test
    public void Given_CodeWithDigitOutRange_When_AskPlayerSecretCode_Then_AskCodeUntilRulesSatisfied() {
        System.setIn(new ByteArrayInputStream("9063\n6053\n5043\n".getBytes()));
        consoleUI = new UserInteraction();
        int[] gameLevel = {4,12,6};
        CodeGenerator codeGenerator = new CodeGeneratorMasterMind(gameLevel);
        Response response = codeGenerator.askPlayerSecretCode(consoleUI);
        assertEquals("5043", response.getValue("code"));
    }

    @Test
    public void Given_CodeWithMoreThanTwoIdenticalDigit_When_AskPlayerSecretCode_Then_AskCodeUntilRulesSatisfied() {
        System.setIn(new ByteArrayInputStream("9063\n76662\n76562\n".getBytes()));
        consoleUI = new UserInteraction();
        int[] gameLevel = {5,12,8};
        CodeGenerator codeGenerator = new CodeGeneratorMasterMind(gameLevel);
        Response response = codeGenerator.askPlayerSecretCode(consoleUI);
        assertEquals("76562", response.getValue("code"));
    }
     */

    /*
     * Tests de la methode askPlayerCodeAttempt

    @Test
    public void Given_CodeMatchingRules_When_askPlayerCodeAttempt_Then_ReturnAValidCode() {
        System.setIn(new ByteArrayInputStream("843970\n".getBytes()));
        consoleUI = new UserInteraction();
        int[] gameLevel = {6,12,10};
        CodeGenerator codeGenerator = new CodeGeneratorMasterMind(gameLevel);
        Response response = codeGenerator.askPlayerCodeAttempt(consoleUI);
        assertEquals("843970", response.getValue("code"));
    }

    @Test
    public void Given_CodeWithWrongLength_When_askPlayerCodeAttempt_Then_AskCodeUntilRulesSatisfied() {
        System.setIn(new ByteArrayInputStream("843970\n74370\n".getBytes()));
        consoleUI = new UserInteraction();
        int[] gameLevel = {5,12,8};
        CodeGenerator codeGenerator = new CodeGeneratorMasterMind(gameLevel);
        Response response = codeGenerator.askPlayerCodeAttempt(consoleUI);
        assertEquals("74370", response.getValue("code"));
    }

    @Test
    public void Given_CodeWithDigitOutRange_When_askPlayerCodeAttempt_Then_AskCodeUntilRulesSatisfied() {
        System.setIn(new ByteArrayInputStream("2843\n2264\n2510\n".getBytes()));
        consoleUI = new UserInteraction();
        int[] gameLevel = {4,12,6};
        CodeGenerator codeGenerator = new CodeGeneratorMasterMind(gameLevel);
        Response response = codeGenerator.askPlayerCodeAttempt(consoleUI);
        assertEquals("2510", response.getValue("code"));
    }

    @Test
    public void Given_CodeWithMoreThanTwoIdenticalDigit_When_askPlayerCodeAttempt_Then_AskCodeUntilRulesSatisfied() {
        System.setIn(new ByteArrayInputStream("877970\n882831\n843970\n".getBytes()));
        consoleUI = new UserInteraction();
        int[] gameLevel = {6,12,10};
        CodeGenerator codeGenerator = new CodeGeneratorMasterMind(gameLevel);
        Response response = codeGenerator.askPlayerCodeAttempt(consoleUI);
        assertEquals("843970", response.getValue("code"));
    }
     */


}