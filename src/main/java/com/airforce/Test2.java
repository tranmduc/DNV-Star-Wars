package com.airforce;

import com.airforce.ui.ScreenManager;

public class Test2 {
    public static void main(String[] args) {
//        GameSetup game = new GameSetup("FuDuSkyWar", 500, 600);
//        game.start();
        ScreenManager window = ScreenManager.getInstance();
        window.display();
    }
}
