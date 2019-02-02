package com.herve.ocgames;

import com.herve.ocgames.core.AppController;
import com.herve.ocgames.core.GameChoice;
import com.herve.ocgames.core.PropertyHelper;
import com.herve.ocgames.utils.StringTool;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Main {

    final public static Logger consoleLogger = Logger.getLogger("root");
    final public static Logger supportLogger = Logger.getLogger("support_file");
    final public static Logger devLogger = Logger.getLogger("development_file");
    final public static Logger devConsoleLogger = Logger.getLogger("development_console");

    public static void main(String[] args) {
        // Initialize property helper (default config -> config file -> command options)
        PropertyHelper.initialize();
        /* If debug is activated, set logger loglevel to debug        */
        if (StringTool.match(PropertyHelper.config("core.debug"),"([tT]rue|1|[yY]es)"))
            devLogger.setLevel(Level.DEBUG);

        // Initialize AppController (control how the application globally runs)
        AppController appController = new AppController();

        appController.showWelcome();
        boolean quitAfterGame = false;
        do {
            // Select Game
            appController.selectGame();
            if (GameChoice.getGameId() == 0 )
                // If game choice is quit, application will be stopped
                appController.setApplicationStopped(true);
            else
                // else game will be launched from controller
                quitAfterGame = appController.launchGame();
        } while (! appController.isApplicationStopped() && ! quitAfterGame);

    }


}
