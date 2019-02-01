package com.herve.ocgames.core.enums;

/*
 * This enum is used to filter content of config.properties
 * enum first member is the key
 * enum second member is a regexp which give the rule to filter value
 * All key value not compliant with enum will be rejected
 */

public enum ConfigEntry {

    CONFIGMODE("config.mode","^(strict|custom)$"),
    COREDEBUG("core.debug", "1|true|yes"),
    CORELANGUAGE("core.language", "^(default|fr_FR|en_EN|test_[0-9])$"),
    COLORDEFENDER("color.defender","^(reset|black|red|green|yellow|blue|purple|cyan|white|bright_black|bright_red" +
            "|bright_green|bright_yellow|bright_blue|bright_purple|bright_cyan|bright_white)$"),
    COLORCHALLENGER("color.challenger","^(reset|black|red|green|yellow|blue|purple|cyan|white|bright_black|bright_red" +
            "|bright_green|bright_yellow|bright_blue|bright_purple|bright_cyan|bright_white)$"),
    COLORRULES("color.rules","^(reset|black|red|green|yellow|blue|purple|cyan|white|bright_black|bright_red" +
            "|bright_green|bright_yellow|bright_blue|bright_purple|bright_cyan|bright_white)$"),
    COLORWINNER("color.winner","^(reset|black|red|green|yellow|blue|purple|cyan|white|bright_black|bright_red" +
            "|bright_green|bright_yellow|bright_blue|bright_purple|bright_cyan|bright_white)$"),
    COLORLOOSER("color.looser","^(reset|black|red|green|yellow|blue|purple|cyan|white|bright_black|bright_red" +
            "|bright_green|bright_yellow|bright_blue|bright_purple|bright_cyan|bright_white)$"),
    MASTERMINDVERSION("mastermind.version",
            "^(mm462|mm463|mm464|mm482|mm483|mm484|mm402|mm403|mm404|mm682|mm683|mm684|mm602|mm603|mm604|mm802|mm803|mm804)$"),
    MASTERMINDCODELENGTH("mastermind.codeLength","^(4|5|6|7|8|9|10)$"),
    MASTERMINDDIGITSINGAME("mastermind.digitsInGame","^(6|7|8|9|10)$"),
    MASTERMINDDIGITMAXREPEAT("mastermind.digitMaxRepeat","^(2|3|4|5|6|7|8|9|10)$"),
    MASTERMINDATTEMPTS("mastermind.attempts", "^(4|5|6|7|8|9|10|12|15|20|25)$"),
    PLUSMOINSVERSION("plusmoins.version","^(pm40x|pm60x|pm80x)$"),
    PLUSMOINSCODELENGTH("mastermind.codeLength","^(4|5|6|7|8|9|10)$"),
    PLUSMOINSATTEMPTS("plusmoins.attempts", "^[2-6]$"),
    UNAVAILABLE("", "");

    /*
    public String key;
    public String valueFilter;
    */


    private String configKey;
    private String configValueFilter;

    ConfigEntry(String configKey, String configValueFilter){
        this.configKey = configKey ;
        this.configValueFilter = configValueFilter;
    }

    public String key(){
        return configKey;
    }

    public String valueFilter(){
        return configValueFilter;
    }

}
