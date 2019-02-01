package com.herve.ocgames.core.factories;

import com.herve.ocgames.core.Player;
import com.herve.ocgames.mastermind.PlayerMasterMind;
import com.herve.ocgames.plusmoins.PlayerPlusMoins;

public class PlayerFactory {

    /**
     * Instanciate object player regarding choice between game MasterMind (id 1 in GameFromList) and PlusMoins (id 2)
     * Player implements global features PlayerMasterMind and PlayerPlusMoins implement specific properties / methods
     * @param gameId   Game id
     * @return Object PlayerMasterMind or PlayerPlusMoins
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
