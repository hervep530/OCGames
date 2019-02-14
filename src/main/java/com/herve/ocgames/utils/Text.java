package com.herve.ocgames.utils;

public class Text {


    // Reset
    public static final String RESET = "\033[0m";  // Text Reset

    // Regular Colors
    public static final String BLACK_COLOR = "\033[30m";   // BLACK
    public static final String RED_COLOR = "\033[31m";     // RED
    public static final String GREEN_COLOR = "\033[32m";   // GREEN
    public static final String YELLOW_COLOR = "\033[33m";  // YELLOW
    public static final String BLUE_COLOR = "\033[34m";    // BLUE
    public static final String PURPLE_COLOR = "\033[35m";  // PURPLE
    public static final String CYAN_COLOR = "\033[36m";    // CYAN
    public static final String WHITE_COLOR = "\033[37m";   // WHITE

    // Bold
    public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
    public static final String RED_BOLD = "\033[1;31m";    // RED
    public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
    public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
    public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
    public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
    public static final String WHITE_BOLD = "\033[1;37m";  // WHITE

    // Underline
    public static final String BLACK_UNDERLINED = "\033[4;30m";  // BLACK
    public static final String RED_UNDERLINED = "\033[4;31m";    // RED
    public static final String GREEN_UNDERLINED = "\033[4;32m";  // GREEN
    public static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
    public static final String BLUE_UNDERLINED = "\033[4;34m";   // BLUE
    public static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
    public static final String CYAN_UNDERLINED = "\033[4;36m";   // CYAN
    public static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE
    public static final String BRIGHT_WHITE_UNDERLINED = "\033[4;97m";  // WHITE

    // Background
    public static final String BLACK_BACKGROUND = "\033[40m";  // BLACK
    public static final String RED_BACKGROUND = "\033[41m";    // RED
    public static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
    public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
    public static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
    public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
    public static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
    public static final String WHITE_BACKGROUND = "\033[47m";  // WHITE

    // High Intensity
    public static final String BRIGHT_BLACK_COLOR = "\033[0;90m";   // BRIGHT_BLACK_COLOR
    public static final String BRIGHT_RED_COLOR = "\033[0;91m";   // BRIGHT_RED_COLOR
    public static final String BRIGHT_GREEN_COLOR = "\033[0;92m";   // BRIGHT_GREEN_COLOR
    public static final String BRIGHT_YELLOW_COLOR = "\033[0;93m";   // BRIGHT_YELLOW_COLOR
    public static final String BRIGHT_BLUE_COLOR = "\033[0;94m";   // BRIGHT_BLUE_COLOR
    public static final String BRIGHT_PURPLE_COLOR = "\033[0;95m";   // BRIGHT_PURPLE_COLOR
    public static final String BRIGHT_CYAN_COLOR = "\033[0;96m";   // BRIGHT_CYAN_COLOR
    public static final String BRIGHT_WHITE_COLOR = "\033[0;97m";   // BRIGHT_WHITE_COLOR

    // Bold High Intensity
    public static final String BRIGHT_BLACK_BOLD = "\033[1;90m"; // BLACK
    public static final String BRIGHT_RED_BOLD = "\033[1;91m";   // RED
    public static final String BRIGHT_GREEN_BOLD = "\033[1;92m"; // GREEN
    public static final String BRIGHT_YELLOW_BOLD = "\033[1;93m";// YELLOW
    public static final String BRIGHT_BLUE_BOLD = "\033[1;94m";  // BLUE
    public static final String BRIGHT_PURPLE_BOLD = "\033[1;95m";// PURPLE
    public static final String BRIGHT_CYAN_BOLD = "\033[1;96m";  // CYAN
    public static final String BRIGHT_WHITE_BOLD = "\033[1;97m"; // WHITE

    // High Intensity backgrounds
    public static final String BRIGHT_BLACK_BACKGROUND = "\033[0;100m";// BLACK
    public static final String BRIGHT_RED_BACKGROUND = "\033[0;101m";// RED
    public static final String BRIGHT_GREEN_BACKGROUND = "\033[0;102m";// GREEN
    public static final String BRIGHT_YELLOW_BACKGROUND = "\033[0;103m";// YELLOW
    public static final String BRIGHT_BLUE_BACKGROUND = "\033[0;104m";// BLUE
    public static final String BRIGHT_PURPLE_BACKGROUND = "\033[0;105m"; // PURPLE
    public static final String BRIGHT_CYAN_BACKGROUND = "\033[0;106m";  // CYAN
    public static final String BRIGHT_WHITE_BACKGROUND = "\033[0;107m";   // WHITE

    public static final String CLEAR_SCREEN = "\033[2J";

    /**
     * Given a text and an avaible color as static Class variable, return a string like COLOR + text + RESET
     * @param text String with text to colorize
     * @param color color prefix as string (for example : bright_red to use the BRIGHT_RED_COLOR value)
     * @return a String with text prefixed by color code and suffixed by reset code
     */
    public static String effect(String text, String color){
        String result = text;
        String effect;
        if (color == null) return text;
        // if color is valid, display text with custom color, else display text with default color (RESET)
        try {
            effect = Text.class.getDeclaredField(color.toUpperCase() + "_COLOR").get(null).toString();
            result = effect + text + RESET;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // invalid color
            return text;
        }
        return result;
    }

    /**
     * Given a text and an avaible color as static Class variable, return a string like COLOR + text + RESET
     * @param text String with text to colorize
     * @param type String which indicate the name of effect (ex : background, underlined,...)
     * @return a String with text prefixed by color / effect code and suffixed by reset code
     */
    public static String effect(String text, String type, String color){
        String result = text;
        String effect;
        if (color == null) return text;
        // if effect is valid (color_type), display text with custom effect, else display text with default color
        try {
            String effectReference = color.toUpperCase() + "_" + type.toUpperCase();
            effect = Text.class.getDeclaredField(effectReference).get(null).toString();
            result = effect + text + RESET;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // invalid effect
            return text;
        }
        return result;
    }

}
