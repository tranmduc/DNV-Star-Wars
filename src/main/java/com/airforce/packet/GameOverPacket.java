package com.airforce.packet;

import java.io.Serializable;
import java.util.ArrayList;

import com.airforce.common.PlayerInGame;

public class GameOverPacket implements Serializable {
    public ArrayList<PlayerInGame> playerInGames;

    public GameOverPacket(ArrayList<PlayerInGame> playerInGames) {
        this.playerInGames = new ArrayList<>();

        this.playerInGames.addAll(playerInGames);
    }
}
