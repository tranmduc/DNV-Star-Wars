package com.airforce.packet;

import java.io.Serializable;
import java.util.ArrayList;

import com.airforce.common.Bullet;
import com.airforce.common.Enemy;
import com.airforce.common.PlayerInGame;

public class UpdateIngameInfoPacket implements Serializable {
    public ArrayList<PlayerInGame> playerInGames;
    public ArrayList<Bullet> bullets;
    public ArrayList<Bullet> enemyBullets;
    public ArrayList<Enemy> enemies;

    public UpdateIngameInfoPacket(ArrayList<PlayerInGame> playerInGames, ArrayList<Bullet> bullets, ArrayList<Bullet> enemyBullets, ArrayList<Enemy> enemies) {
        this.playerInGames = new ArrayList<>();
        this.bullets = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.enemyBullets = new ArrayList<>();

        this.playerInGames.addAll(playerInGames);
        this.bullets.addAll(bullets);
        this.enemies.addAll(enemies);
        this.enemyBullets.addAll(enemyBullets);
    }
}
