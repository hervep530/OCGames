package com.herve.ocgames.core.enums;

/*
 * This enum provide a list of game version to restrict configuration. It's only used with config mode "STRICT"
 * It 's particularly relevant to implement computer strategy in defender mode. It's a way to limit nomber of strategies
 * and associated files
 * Of course, at the opposite, config mode "CUSTOM" allow to use more versions. But with this config mode it's not possible
 * to implement strategies
 */


public enum GameVersion {

    PM40X ("pm40x", "plusmoins", "4", "10", "10","000"),
    PM60X ("pm60x", "plusmoins", "6", "10", "10","000"),
    PM80X ("pm80x", "plusmoins", "8", "10", "10","000"),
    MM462 ("mm462", "mastermind", "4", "6", "2","000"),
    MM463 ("mm463", "mastermind", "4", "6", "3","000"),
    MM464 ("mm464", "mastermind", "4", "6", "4","000"),
    MM482 ("mm482", "mastermind", "4", "8", "2","000"),
    MM483 ("mm483", "mastermind", "4", "8", "3","000"),
    MM484 ("mm484", "mastermind", "4", "8", "4","000"),
    MM402 ("mm402", "mastermind", "4", "10", "2","000"),
    MM403 ("mm403", "mastermind", "4", "10", "3","000"),
    MM404 ("mm404", "mastermind", "4", "10", "4","000"),
    MM682 ("mm682", "mastermind", "6", "8", "2","000"),
    MM683 ("mm682", "mastermind", "6", "8", "3","000"),
    MM684 ("mm682", "mastermind", "6", "8", "4","000"),
    MM602 ("mm602", "mastermind", "6", "10", "2","000"),
    MM603 ("mm603", "mastermind", "6", "10", "3","000"),
    MM604 ("mm604", "mastermind", "6", "10", "4","000"),
    MM802 ("mm802", "mastermind", "8", "10", "2","000"),
    MM803 ("mm803", "mastermind", "8", "10", "3","000"),
    MM804 ("mm804", "mastermind", "8", "10", "4","000"),
    UNAVAILABLE ("", "", "", "", "","");

    private String versionName;
    private String gameName;
    private String codeLength;
    private String digitsInGame;
    private String digitsMaxRepeat;
    private String primaryStrategy;

    GameVersion(String name, String gameName, String codeLength, String digitInGame, String digitMaxRepeat,
                String primaryStrategy) {
        this.versionName = name;
        this.gameName = gameName;
        this.codeLength = codeLength;
        this.digitsInGame = digitInGame;
        this.digitsMaxRepeat = digitMaxRepeat;
        this.primaryStrategy = primaryStrategy;
    }

    public String version(){
        return this.versionName;
    }

    public String game(){
        return gameName;
    }

    public String getCodeLength(){
        return codeLength;
    }

    public String getDigitsInGame(){
        return digitsInGame;
    }

    public String getDigitsMaxRepeat(){
        return digitsMaxRepeat;
    }

    public String getPrimaryStrategy(){
        return primaryStrategy;
    }

    public String summary(){
        return "";
    }

    public String rules(){
        return "";
    }

    public String toString(){
        return versionName;
    }


}
