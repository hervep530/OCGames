package com.herve.ocgames.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.AppenderControl;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.jmx.AppenderAdmin;
import org.apache.logging.log4j.core.jmx.LoggerConfigAdmin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileToolTest {

    private static final Logger consoleLogger = LogManager.getLogger("development_console");
    private static final Level devLogLevel = LogManager.getLogger("development_file").getLevel();
    private static final String devLogFile = "log/OCGames_developpement.log";
    private static final String supportLogFile = "log/OCGames.log";
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

/*
            FileTool.getArrayListFromFile(fileName).stream()
                    .filter(property->StringTool.match(property[1],
                            ConfigEntry.valueOf(property[0].replaceAll("\\.","").toUpperCase()).valueFilter()))
                    .forEach(property->configRepository.put(property[0],property[1]));
*/


    @BeforeEach
    public void setUpStreams() {
        final LoggerContext context = LoggerContext.getContext(false);
        final Configuration config = context.getConfiguration();
        Appender devFileAppender = config.getAppender("devFile");
        clearDevelopmentLog();
        ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addAppender(devFileAppender);
        ((org.apache.logging.log4j.core.Logger) LogManager.getLogger("support_file")).addAppender(devFileAppender);
        ((org.apache.logging.log4j.core.Logger) LogManager.getLogger("development_file")).setLevel(Level.DEBUG);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        final LoggerContext context = LoggerContext.getContext(false);
        final Configuration config = context.getConfiguration();
        Appender devFileAppender = config.getAppender("devFile");
        ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).removeAppender(devFileAppender);
        ((org.apache.logging.log4j.core.Logger) LogManager.getLogger("support_file")).removeAppender(devFileAppender);
        ((org.apache.logging.log4j.core.Logger) LogManager.getLogger("development_file")).setLevel(devLogLevel);
        System.setOut(System.out);
    }

    @Test
    void Given_FileDoesntExist_When_GetArrayListFromFile_Then_ArrayListIsNullAndLogFileError(){
        ArrayList<String[]> properties = FileTool.getArrayListFromFile("file_doesnrt_exist.properties");
        assertTrue(logFound("File file_doesnt_exist.properties doesn't exist", devLogFile));
        assertTrue(properties == null);
    }

    @Test
    void Given_FileWithEmptyValue_When_GetArrayListFromFile_Then_ArrayListContainsKeyEvenIfValueIsEmpty(){
        ArrayList<String[]> properties = FileTool.getArrayListFromFile("src/test/resources/key_with_empty_value_config.properties");
        Map<String,String> mapProperties = new HashMap<>();
        mapProperties.put("core.debug", "true");
        mapProperties.put("core.language", "");
        properties.stream().forEach(property->assertTrue(mapProperties.get(property[0]).contentEquals(property[1])));
    }

    @Test
    void Given_FileWithIllegalCharInKey_When_GetArrayListFromFile_Then_ArrayListIgnoreInvalidKey(){
        ArrayList<String[]> properties = FileTool.getArrayListFromFile("src/test/resources/key_with_illegal_char_config.properties");
        Map<String,String> mapProperties = new HashMap<>();
        mapProperties.put("core.debug", "true");
        mapProperties.put("plus-moins.attempts", "4");
        mapProperties.put("mastermind.attempts", "12");
        properties.stream().forEach(property->assertTrue(mapProperties.get(property[0]).contentEquals(property[1])));
    }

    @Test
    void Given_FileWithIllegalCharInValue_When_GetArrayListFromFile_Then_ArrayListIgnoreInvalidKey(){
        ArrayList<String[]> properties = FileTool.getArrayListFromFile("src/test/resources/value_with_special_char_config.properties");
        Map<String,String> mapProperties = new HashMap<>();
        mapProperties.put("core.debug", "true");
        mapProperties.put("core.language", "tes-t_1");
        mapProperties.put("color.rules", "whi+te");
        mapProperties.put("color.winner", "gre!en");
        mapProperties.put("plusmoins.attempts", "$4");
        mapProperties.put("mastermind.attempts", "1/2");
        properties.stream().forEach(property->assertTrue(mapProperties.get(property[0]).contentEquals(property[1])));
    }

    @Test
    void Given_FileWithNullValue_When_GetArrayListFromFile_Then_ArrayListIgnoreKeyWithNullValue(){
        ArrayList<String[]> properties = FileTool.getArrayListFromFile("src/test/resources/key_without_value_config.properties");
        Map<String,String> mapProperties = new HashMap<>();
        mapProperties.put("core.debug", "true");
        properties.stream().forEach(property->assertTrue(mapProperties.get(property[0]).contentEquals(property[1])));
    }

    @Test
    void Given_RightFile_When_GetArrayListFromFile_Then_ArrayListHasCorrectKeyValue(){
        ArrayList<String[]> properties = FileTool.getArrayListFromFile("src/test/resources/right_config.properties");
        Map<String,String> mapProperties = new HashMap<>();
        mapProperties.put("core.debug", "true");
        mapProperties.put("core.language", "test_1");
        mapProperties.put("color.rules", "white");
        properties.stream().forEach(property->assertTrue(mapProperties.get(property[0]).contentEquals(property[1])));
    }

    @Test
    void Given_FileWithValueQuoted_When_GetArrayListFromFile_Then_RemoveExternalValueQuotesAndKeepQuoteInsideValue(){
        ArrayList<String[]> properties = FileTool.getArrayListFromFile("src/test/resources/value_with_quote_config.properties");
        Map<String,String> mapProperties = new HashMap<>();
        mapProperties.put("core.debug", "true");
        mapProperties.put("core.language", "test_1");
        mapProperties.put("color.rules", "white");
        mapProperties.put("color.winner", "green");
        mapProperties.put("message.surprised", "he says me \"why\", and I answer because...");
        properties.stream().forEach(property->assertTrue(mapProperties.get(property[0]).contentEquals(property[1])));
    }

    @Test
    void Given_FileWithSingleQuote_When_GetArrayListFromFile_Then_ArrayListIsKeepingSingleQuoteInValue(){
        ArrayList<String[]> properties = FileTool.getArrayListFromFile("src/test/resources/value_with_SingleQuote_config.properties");
        Map<String,String> mapProperties = new HashMap<>();
        mapProperties.put("core.debug", "'true'");
        mapProperties.put("core.language", "test_1");
        properties.stream().forEach(property->assertTrue(mapProperties.get(property[0]).contentEquals(property[1])));
    }

    @Test
    void Given_FileWithComments_When_GetArrayListFromFile_Then_ArrayListContainsKeyValueAndIgnoreComments(){
        ArrayList<String[]> properties = FileTool.getArrayListFromFile("src/test/resources/with_comments_config.properties");
        Map<String,String> mapProperties = new HashMap<>();
        mapProperties.put("core.debug", "true");
        mapProperties.put("color.rules", "white");
        mapProperties.put("color.winner", "green");
        mapProperties.put("message.test", "j'aime les # a la folie");
        properties.stream().forEach(property->assertTrue(mapProperties.get(property[0]).contentEquals(property[1])));
    }

    @Test
    void Given_FileWithValueContainingEqualSymbol_When_GetArrayListFromFile_Then_ArrayListAcceptEqualInValue(){
        ArrayList<String[]> properties = FileTool.getArrayListFromFile("src/test/resources/with_double_equal_config.properties");
        Map<String,String> mapProperties = new HashMap<>();
        mapProperties.put("core.debug", "true");
        mapProperties.put("core.language", "test_1 = fr_FR");
        properties.stream().forEach(property->assertTrue(mapProperties.get(property[0]).contentEquals(property[1])));
    }

    @Test
    void Given_FileWithWrongKeys_When_GetArrayListFromFile_Then_ArrayListIgnoreWrongKeys(){
        ArrayList<String[]> properties = FileTool.getArrayListFromFile("src/test/resources/with_wrong_key_config.properties");
        Map<String,String> mapProperties = new HashMap<>();
        mapProperties.put("color.winner", "green");
        properties.stream().forEach(property->assertTrue(mapProperties.get(property[0]).contentEquals(property[1])));
    }

    /*
        properties.stream().forEach(property->consoleLogger.debug(property[0] + " -> " + property[1]));
        for (Map.Entry<String,String> entry : properties.entrySet()){
            consoleLogger.debug(entry.getKey() + " -> " + entry.getValue());
        }
    */

    @Test
    void Given_FileDoesntExist_When_GetMapFromFile_Then_ArrayListIsNullAndLogFileError(){
        Map<String,String> properties = FileTool.getMapFromFile("file_doesnt_exist.properties");
        assertTrue(logFound("File file_doesnt_exist.properties doesn't exist", devLogFile));
        assertTrue(properties == null);
    }

    @Test
    void Given_FileWithEmptyValue_When_GetMapFromFile_Then_ArrayListContainsKeyEvenIfValueIsEmpty(){
        Map<String,String> properties = FileTool.getMapFromFile("src/test/resources/key_with_empty_value_config.properties");
        Map<String,String> refProperties = new HashMap<>();
        refProperties.put("core.debug", "true");
        refProperties.put("core.language", "");
        for (Map.Entry<String,String> entry : properties.entrySet()){
            assertTrue(refProperties.get(entry.getKey()).contentEquals(entry.getValue()));
        }
    }

    @Test
    void Given_FileWithIllegalCharInKey_When_GetMapFromFile_Then_ArrayListIgnoreInvalidKey(){
        Map<String,String> properties = FileTool.getMapFromFile("src/test/resources/key_with_illegal_char_config.properties");
        Map<String,String> refProperties = new HashMap<>();
        refProperties.put("core.debug", "true");
        refProperties.put("plus-moins.attempts", "4");
        refProperties.put("mastermind.attempts", "12");
        for (Map.Entry<String,String> entry : properties.entrySet()){
            assertTrue(refProperties.get(entry.getKey()).contentEquals(entry.getValue()));
        }
    }

    @Test
    void Given_FileWithIllegalCharInValue_When_GetMapFromFile_Then_ArrayListIgnoreInvalidKey(){
        Map<String,String> properties = FileTool.getMapFromFile("src/test/resources/value_with_special_char_config.properties");
        Map<String,String> refProperties = new HashMap<>();
        refProperties.put("core.debug", "true");
        refProperties.put("core.language", "tes-t_1");
        refProperties.put("color.rules", "whi+te");
        refProperties.put("color.winner", "gre!en");
        refProperties.put("plusmoins.attempts", "$4");
        refProperties.put("mastermind.attempts", "1/2");
        for (Map.Entry<String,String> entry : properties.entrySet()){
            assertTrue(refProperties.get(entry.getKey()).contentEquals(entry.getValue()));
        }
    }

    @Test
    void Given_FileWithNullValue_When_GetMapFromFile_Then_ArrayListIgnoreKeyWithNullValue(){
        Map<String,String> properties = FileTool.getMapFromFile("src/test/resources/key_without_value_config.properties");
        Map<String,String> refProperties = new HashMap<>();
        refProperties.put("core.debug", "true");
        for (Map.Entry<String,String> entry : properties.entrySet()){
            assertTrue(refProperties.get(entry.getKey()).contentEquals(entry.getValue()));
        }
    }

    @Test
    void Given_RightFile_When_GetMapFromFile_Then_ArrayListHasCorrectKeyValue(){
        Map<String,String> properties = FileTool.getMapFromFile("src/test/resources/right_config.properties");
        Map<String,String> refProperties = new HashMap<>();
        refProperties.put("core.debug", "true");
        refProperties.put("core.language", "test_1");
        refProperties.put("color.rules", "white");
        for (Map.Entry<String,String> entry : properties.entrySet()){
            assertTrue(refProperties.get(entry.getKey()).contentEquals(entry.getValue()));
        }
    }

    @Test
    void Given_FileWithValueQuoted_When_GetMapFromFile_Then_RemoveExternalValueQuotesAndKeepQuoteInsideValue(){
        Map<String,String> properties = FileTool.getMapFromFile("src/test/resources/value_with_quote_config.properties");
        Map<String,String> refProperties = new HashMap<>();
        refProperties.put("core.debug", "true");
        refProperties.put("core.language", "test_1");
        refProperties.put("color.rules", "white");
        refProperties.put("color.winner", "green");
        refProperties.put("message.surprised", "he says me \"why\", and I answer because...");
        for (Map.Entry<String,String> entry : properties.entrySet()){
            assertTrue(refProperties.get(entry.getKey()).contentEquals(entry.getValue()));
        }
    }

    @Test
    void Given_FileWithSingleQuote_When_GetMapFromFile_Then_ArrayListIsKeepingSingleQuoteInValue(){
        Map<String,String> properties = FileTool.getMapFromFile("src/test/resources/value_with_SingleQuote_config.properties");
        Map<String,String> refProperties = new HashMap<>();
        refProperties.put("core.debug", "'true'");
        refProperties.put("core.language", "test_1");
        for (Map.Entry<String,String> entry : properties.entrySet()){
            assertTrue(refProperties.get(entry.getKey()).contentEquals(entry.getValue()));
        }
    }

    @Test
    void Given_FileWithComments_When_GetMapFromFile_Then_ArrayListContainsKeyValueAndIgnoreComments(){
        Map<String,String> properties = FileTool.getMapFromFile("src/test/resources/with_comments_config.properties");
        Map<String,String> refProperties = new HashMap<>();
        refProperties.put("core.debug", "true");
        refProperties.put("color.rules", "white");
        refProperties.put("color.winner", "green");
        refProperties.put("message.test", "j'aime les # a la folie");
        for (Map.Entry<String,String> entry : properties.entrySet()){
            assertTrue(refProperties.get(entry.getKey()).contentEquals(entry.getValue()));
        }
    }

    @Test
    void Given_FileWithValueContainingEqualSymbol_When_GetMapFromFile_Then_ArrayListAcceptEqualInValue(){
        Map<String,String> properties = FileTool.getMapFromFile("src/test/resources/with_double_equal_config.properties");
        Map<String,String> refProperties = new HashMap<>();
        refProperties.put("core.debug", "true");
        refProperties.put("core.language", "test_1 = fr_FR");
        for (Map.Entry<String,String> entry : properties.entrySet()){
            assertTrue(refProperties.get(entry.getKey()).contentEquals(entry.getValue()));
        }
    }

    @Test
    void Given_FileWithWrongKeys_When_GetMapFromFile_Then_ArrayListIgnoreWrongKeys(){
        Map<String,String> properties = FileTool.getMapFromFile("src/test/resources/with_wrong_key_config.properties");
        Map<String,String> refProperties = new HashMap<>();
        refProperties.put("color.winner", "green");
        for (Map.Entry<String,String> entry : properties.entrySet()){
            assertTrue(refProperties.get(entry.getKey()).contentEquals(entry.getValue()));
        }
    }

/*
    @Test
    void Given__When_GetArrayListFromFile_Then_ArrayListContainsKeyValueAndIgnore(){
        Map<String,String> mapToConvert = new HashMap<>();
        mapToConvert.put("aKey", "");
        String[][] result = MapTool.convertStringMapToStringArray(mapToConvert, false);
        for (int i = 0 ; i < result.length ; i++){
            assertTrue(mapToConvert.containsKey(result[i][0]));
            assertTrue(result[i][1].contentEquals(mapToConvert.get(result[i][0])));
        }
    }

    //LOG  :    properties.stream().forEach(property->consoleLogger.debug(property[0] + " -> " + property[1]));


*/





}