package com.herve.ocgames.utils;

import com.herve.ocgames.core.exceptions.InvalidAliasKey;
import com.herve.ocgames.core.exceptions.InvalidArrayOption;
import com.herve.ocgames.core.exceptions.InvalidDigitParametersForMatchingMethod;
import com.herve.ocgames.core.exceptions.InvalidMapConverter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapToolTest {

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

    private Map<String,String> setMapValues(){
        Map<String, String> mapValues = new HashMap<>();
        mapValues.put("eat","eat");
        mapValues.put("e","eat");
        mapValues.put("sleep","sleep");
        mapValues.put("e","sleep");
        mapValues.put("drink","drink");
        mapValues.put("d","drink");
        mapValues.put("work","work");
        mapValues.put("w","work");
        return mapValues;
    }


    /*
    Tests for method ConvertFlatStringArrayToMap
     */

    @Test
    void Given_MapWithAtLeastOneKeyAndEmptyValue_When_convertStringMapToStringArray_Then_ReturnBidimensionnalStringArray(){
        Map<String,String> mapToConvert = new HashMap<>();
        mapToConvert.put("aKey", "");
        String[][] result = MapTool.convertStringMapToStringArray(mapToConvert, false);
        for (int i = 0 ; i < result.length ; i++){
            assertTrue(mapToConvert.containsKey(result[i][0]));
            assertTrue(result[i][1].contentEquals(mapToConvert.get(result[i][0])));
        }
    }

    @Test
    void Given_MapWithSeveralKeys_When_convertStringMapToStringArray_Then_ReturnBidimensionnalStringArray(){
        Map<String,String> mapToConvert = setMapValues();
        String[][] result = MapTool.convertStringMapToStringArray(mapToConvert, false);
        for (int i = 0 ; i < result.length ; i++){
            assertTrue(mapToConvert.containsKey(result[i][0]));
            assertTrue(result[i][1].contentEquals(mapToConvert.get(result[i][0])));
        }
    }

    @Test
    void Given_NullMap_When_convertStringMapToStringArray_Then_ThrowExceptionAndLogError(){
        Map<String,String> mapToConvert = null;
        assertThrows(InvalidMapConverter.class,
                () -> MapTool.convertStringMapToStringArray(mapToConvert, true));
        assertTrue(logFound("You can't use this method with null mapConverter", devLogFile));
    }

    @Test
    void Given_EmptyMap_When_convertStringMapToStringArray_Then_ThrowExceptionAndLogError(){
        Map<String,String> mapToConvert = new HashMap<>();
        assertThrows(InvalidMapConverter.class,
                () -> MapTool.convertStringMapToStringArray(mapToConvert, true));
        assertTrue(logFound("You can't use this method with empty mapConverter", devLogFile));
    }


    /*
    Tests for method ConvertFlatStringArrayToMap
     */

    @Test
    void Given_RightConverterAndArrayFinishingKeyValue_When_ConvertFlatStringArrayToMap_Then_ReturnMapWithAllArrayEntries(){
        Map<String,String> converter = setMapValues();
        String[] options = new String[] {"--eat", "patatoes", "--nosleep", "-w", "-d", "water"};
        Map<String,String> convertedOptionsMap = MapTool.convertFlatStringArrayToMap(converter,options, false);
        assertTrue(convertedOptionsMap.get("eat").contentEquals("patatoes"));
        assertTrue(convertedOptionsMap.get("sleep").contentEquals("0"));
        assertTrue(convertedOptionsMap.get("work").contentEquals("1"));
        assertTrue(convertedOptionsMap.get("drink").contentEquals("water"));
    }

    @Test
    void Given_RightConverterAndArrayFinishingLoneKey_When_ConvertFlatStringArrayToMap_Then_ReturnMapWithAllArrayEntries(){
        Map<String,String> converter = setMapValues();
        String[] options = new String[] {"--eat", "patatoes", "--nosleep", "-d", "water", "-w"};
        Map<String,String> convertedOptionsMap = MapTool.convertFlatStringArrayToMap(converter,options, false);
        assertTrue(convertedOptionsMap.get("eat").contentEquals("patatoes"));
        assertTrue(convertedOptionsMap.get("sleep").contentEquals("0"));
        assertTrue(convertedOptionsMap.get("work").contentEquals("1"));
        assertTrue(convertedOptionsMap.get("drink").contentEquals("water"));
    }

    @Test
    void Given_RightConverterAndArrayBeginningAndFinishingLoneKey_When_ConvertFlatStringArrayToMap_Then_ReturnMapWithAllArrayEntries(){
        Map<String,String> converter = setMapValues();
        String[] options = new String[] {"--nosleep", "--eat", "patatoes", "-d", "water", "-w"};
        Map<String,String> convertedOptionsMap = MapTool.convertFlatStringArrayToMap(converter,options, false);
        assertTrue(convertedOptionsMap.get("eat").contentEquals("patatoes"));
        assertTrue(convertedOptionsMap.get("sleep").contentEquals("0"));
        assertTrue(convertedOptionsMap.get("work").contentEquals("1"));
        assertTrue(convertedOptionsMap.get("drink").contentEquals("water"));
    }

    /*
    Array Errors Management
     */


    @Test
    void Given_ConverterAndArrayNotCompliant_When_ConvertFlatStringArrayToMap_Then_TrowExceptionAndLogFatal(){
        Map<String,String> converter = setMapValues();
        String[] options = new String[] {"-d", "water", "--norun", "--broke", "glass", "-w"};
        assertThrows(InvalidAliasKey.class,
                () -> MapTool.convertFlatStringArrayToMap(converter,options, true));
        assertTrue(logFound("Invalid option : ", devLogFile));
    }

    @Test
    void Given_RightConverterAndNullArray_When_ConvertFlatStringArrayToMap_Then_ReturnMapWithAllArrayEntries(){
        Map<String,String> converter = setMapValues();
        String[] options = null;
        Map<String,String> convertedOptionsMap = MapTool.convertFlatStringArrayToMap(converter,options, true);
        assertTrue(convertedOptionsMap.isEmpty());
        assertTrue(logFound("Usage of this method with null flat array is not recommended", devLogFile));
    }

    @Test
    void Given_RightConverterAndFirstCrazyArray_When_ConvertFlatStringArrayToMap_Then_ReturnMapWithAllArrayEntries(){
        Map<String,String> converter = setMapValues();
        String[] options = new String[3];
        assertThrows(InvalidArrayOption.class,
                () -> MapTool.convertFlatStringArrayToMap(converter,options, true));
        assertTrue(logFound("Invalid array option", devLogFile));
    }

    @Test
    void Given_RightConverterAnd2ndCrazyArray_When_ConvertFlatStringArrayToMap_Then_ReturnMapWithAllArrayEntries(){
        Map<String,String> converter = setMapValues();
        String[] options = new String[]{};
        Map<String,String> convertedOptionsMap = MapTool.convertFlatStringArrayToMap(converter,options, true);
        assertTrue(convertedOptionsMap.isEmpty());
        assertTrue(logFound("In method convertFlatStringArrayToMap, array option is empty", devLogFile));
    }

    @Test
    void Given_NullConverterAndNoEmptyArray_When_ConvertFlatStringArrayToMap_Then_ReturnMapWithAllArrayEntries(){
        Map<String,String> converter = null;
        String[] options = new String[] {"--nosleep", "--eat", "patatoes", "-d", "water", "-w"};
        assertThrows(InvalidMapConverter.class,
                () -> MapTool.convertFlatStringArrayToMap(converter,options, true));
        assertTrue(logFound("You can't use this method with null mapConverter", devLogFile));
    }

    @Test
    void Given_EmptyConverterAndNoEmptyArray_When_ConvertFlatStringArrayToMap_Then_ReturnMapWithAllArrayEntries(){
        Map<String,String> converter = new HashMap<>();
        String[] options = new String[] {"--nosleep", "--eat", "patatoes", "-d", "water", "-w"};
        assertThrows(InvalidMapConverter.class,
                () -> MapTool.convertFlatStringArrayToMap(converter,options, true));
        assertTrue(logFound("You can't use this method with empty mapConverter", devLogFile));
    }

}

