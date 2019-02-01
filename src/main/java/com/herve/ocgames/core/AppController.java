package com.herve.ocgames.core;

/*
 * AppController Class globally control how the application runs
 */

import com.herve.ocgames.core.enums.GameFromList;
import com.herve.ocgames.core.interfaces.AppControllerInterface;
import com.herve.ocgames.utils.StringTool;
import com.herve.ocgames.utils.Text;
import com.herve.ocgames.utils.UserInteraction;

import static com.herve.ocgames.Main.devLogger;

public class AppController implements AppControllerInterface {

    private boolean applicationStopped ;
    private boolean debug = false;
    private int debugVerbosity = 2 ;



    public AppController() {
        // Active the application and load settings
        this.applicationStopped = false;
        this.debug = StringTool.match(PropertyHelper.config("core.debug"), "^([Tt]rue|[Yy]es|1)$");
    }

    public boolean isApplicationStopped() {
        return applicationStopped;
    }

    public void setApplicationStopped(boolean applicationStopped) {
        this.applicationStopped = applicationStopped;
    }

    private static String lang(String key){
        return PropertyHelper.language(key);
    }


    public void showWelcome(){
        UserInteraction.displayMessage(Text.effect(lang("app.welcome"), "purple"));
    }

    /**
     * Select Game , mode and store it in GameOptions
     */
    public void selectGame() {
        // Set array of menu entries from enum GameFromList
        String[] gameResponses = {GameFromList.JEU1.getMenuEntry(), GameFromList.JEU2.getMenuEntry(),
                lang("select.applicationQuit")};
        debugV3("Get game id with promptSelect");
        int gameId = UserInteraction.promptSelect(lang("category.game"), gameResponses, false);
        debugV2(lang("debug.menuJeu") + gameId);
        if (gameId == gameResponses.length) {
            // We want "quit" at the end of the menu with constant id, so we choose 0 witch is never used by promptSelect
            GameChoice.setGameId(0);
            UserInteraction.displayMessage(lang("application.quit"));
        } else {
            debugV3("if we don't quit, we have a second promptSelect to choose the game mode (challenger, defender, dual)");
            GameChoice.setGameId(gameId);
            String[] modeResponses = {lang("select.challenger"), lang("select.defender"),
                    lang("select.dual")};
            int gameMode = UserInteraction.promptSelect(lang("category.mode"), modeResponses, false);
            debugV2(lang("debug.menuMode") + gameMode);
            GameChoice.setGameMode(gameMode);
        }
    }

    /**
     * Lance le jeu choisi
     * @return   quitAfterGame : retourne true pour quitter l'application après le jeu, false pour rester dans l'application
     */
    public boolean launchGame() {
        // Instanciate boolean out of the loop and set menu entries for the end of the game
        boolean playAgain;
        boolean quitAfterGame = false;
        String[] responses = {lang("select.replay"), lang("select.otherGame"), lang("select.quit")};

        do {
            playAgain = false;
            // Launch the game
            Game game = new Game();
            game.start();
            // Ask what we want to do at the end of the game
            int replay = UserInteraction.promptSelect(lang("category.option"), responses, false);
            debugV2(lang("debug.menuReplay") + replay);
            if (replay == 1) playAgain = true;
            if (replay == 3) {
                quitAfterGame = true;
                UserInteraction.displayMessage(lang("application.quit"));
            }
        } while (playAgain) ;
        // on retourne true si on a choisi de quitter l'application
        return quitAfterGame;
    }

    // debug when verbosity level is equal to 1 - Should be used to exceptionnaly log debug message in the console
    //private void debugV1(String message){ if (this.debug && this.debugVerbosity > 0) devConsoleLogger.debug(message); }

    private void debugV2(String message){
        // debug when verbosity level is up to 2 - Should be used to log computed value in file
        if (this.debug && this.debugVerbosity > 1) devLogger.debug(message);
    }

    private void debugV3(String message){
        // debug when verbosity level is up to 3 - Should be used to log message as comment in the code
        if (this.debug && this.debugVerbosity > 2) devLogger.debug(message);
    }

    // debug when verbosity level is up to 4 - Should be exceptionnaly used to log computed value in loop
    //private void debugV4(String message){ if (this.debug && this.debugVerbosity > 3) devLogger.debug(message); }

}
