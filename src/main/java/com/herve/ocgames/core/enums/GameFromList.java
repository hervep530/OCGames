package com.herve.ocgames.core.enums;

/*
 * This enum is a list of all games in the application
 * the first member is id
 * the second is the game name
 * the third is the language key for menu entry when promptSelect is used
 */

import com.herve.ocgames.core.PropertyHelper;

public enum GameFromList {

    JEU1(1,"mastermind","select.game"),
    JEU2(2,"plusmoins","select.game");

    private int gameId;
    private String gameName;
    private String gameMenu;

    GameFromList(int id, String name, String menuEntry){
        this.gameId= id;
        this.gameName = name ;
        this.gameMenu = menuEntry ;
    }

    public int getId(){
        return this.gameId;
    }

    public String getName(){
        return this.gameName;
    }

    public String getMenuEntry() {
        String menuEntry = PropertyHelper.language(this.gameMenu) + gameName;
        return menuEntry;
    }

    public String toString() {
        return this.gameName;
    }

}
