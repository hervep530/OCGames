package com.herve.ocgames.core;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class PropertyHelperTest {

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

    @Test
    public void Given_Nothing_When_InitializeAndDisplay_Then_DisplayConfigProperties(){
        PropertyHelper.initialize();
        PropertyHelper.loadGame("jeu1");
        PropertyHelper.displayProperties("");
    }

    @Test
    public void Given_Nothing_When_GetAndDisplayConfig_Then_OnlyDisplayConfig(){
        PropertyHelper.initialize();
        PropertyHelper.loadGame("jeu1");
        //PropertyHelper.config().forEach((k,v)-> System.out.println(k + " : " + v));
    }

    @Test
    public void Given_Nothing_When_GetAndDisplayLanguage_Then_OnlyDisplayLanguage(){
        PropertyHelper.initialize();
        // PropertyHelper.language().forEach((k,v)-> System.out.println(k + " : " + v));
    }

    @Test
    public void Given_Key_When_GetAndDisplayConfig_Then_OnlyDisplayTheConfigPropertyValue (){
        PropertyHelper.initialize();
        System.out.println(PropertyHelper.config("core.language"));
    }

    @Test
    public void Given_BadStrategy_When_GetGameConfig_Then_ThrowException (){
        //ConfigEntry param = ConfigEntry.;
    }


}