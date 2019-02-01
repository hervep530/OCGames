package com.herve.ocgames.core.factories;

import com.herve.ocgames.core.Player;
import com.herve.ocgames.mastermind.PlayerMasterMind;
import com.herve.ocgames.plusmoins.PlayerPlusMoins;

public class PlayerFactory {

    /**
     * Permet d'instancier l'objet player en fonction du jeu choisi
     * Généralisation dans la classe Player, spécialisation à partir des classes JeuPlayer
     * @param gameId   Id du jeu
     * @return PlayerMasterMind or PlayerPlusMoins
     */
    public static Player getPlayer(int gameId){
        switch (gameId){
            case 1:
                return new PlayerMasterMind();
            case 2:
                return new PlayerPlusMoins();
        }
        return null;
    }
}
