package com.herve.ocgames.core;

/*
 * Class to store choice about game and mode
 * static getters provide access anywhere
 */

public class GameChoice {
    private static int gameId;
    private static int gameMode;


    public static void initialize() {
        gameId = -1;
        gameMode = -1;
    }


    public static int getGameId() {
        return gameId;
    }

    public static void setGameId(int id) {
        gameId = id;
    }

    public static int getGameMode() {
        return gameMode;
    }

    public static void setGameMode(int mode) {
        gameMode = mode;
    }

    public static String getName() {return "";}

}
