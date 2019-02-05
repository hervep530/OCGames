package com.herve.ocgames;

import com.herve.ocgames.core.AppController;
import com.herve.ocgames.core.GameChoice;
import com.herve.ocgames.core.PropertyHelper;
import com.herve.ocgames.utils.StringTool;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class Main {

    final public static Logger consoleLogger = LogManager.getLogger("root");
    final public static Logger supportLogger = LogManager.getLogger("support_file");
    final public static Logger devConsoleLogger = LogManager.getLogger("development_console");

    public static void main(String[] args) {
        // Initialize property helper (default config -> config file -> command options)
        PropertyHelper.initialize(args);
        /* If debug is activated, set logger loglevel to debug        */
        if (StringTool.match(PropertyHelper.config("core.debug"),"([tT]rue|1|[yY]es)"))
            Configurator.setLevel(devConsoleLogger.getName(), Level.DEBUG);

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
