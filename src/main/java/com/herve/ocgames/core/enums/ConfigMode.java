package com.herve.ocgames.core.enums;

/*
 * ConfigMode enum is an advanced filter for config property config.mode
 * the first member is the value name and the second is a boolean status. Associated to the getter isStrict, it's usefull
 * to test config.mode
 */

public enum ConfigMode {
    STRICT("strict", true),
    CUSTOM("custom", false);

    private String name;
    private boolean strict;

    ConfigMode(String name, boolean strict){
        this.name = name;
        this.strict = strict ;
    }

    public String getName(){
        return this.name;
    }

    public boolean isStrict(){
        return this.strict;
    }

}
